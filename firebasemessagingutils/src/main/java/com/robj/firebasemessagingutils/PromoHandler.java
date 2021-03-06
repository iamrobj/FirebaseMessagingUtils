package com.robj.firebasemessagingutils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jj on 16/10/17.
 */

public class PromoHandler {

    private static final String TAG = PromoHandler.class.getSimpleName();

    private static final int ID_FIREBASE = 1235;

    private final Context context;
    private final String channelId;
    @DrawableRes
    private final int defaultIcon;
    private List<Rule> rules = new ArrayList();
    private List<KeyRule> keyRules = new ArrayList();
    private PendingIntentCreator pendingIntentCreator;

    private PromoHandler(Builder builder) {
        this.context = builder.context;
        this.channelId = builder.channelId;
        this.defaultIcon = builder.defaultIcon;
        this.rules = builder.rules;
        this.keyRules = builder.keyRules;
        this.pendingIntentCreator = builder.pendingIntentCreator;
    }

    private Context getContext() {
        return context;
    }

    public void onMessageReceived(String title, String body, Map<String, String> data) {
        try {
            Log.i(TAG, "FCM received: " + data.toString());
            for(Rule rule : rules)
                if(!rule.isRuleMet()) {
                    Log.i(TAG, "A non key based rule was not satisfied..");
                    return;
                }
            for(String s : data.keySet()) {
                String value = data.get(s);
                boolean isCustomRule = false;
                for(KeyRule rule : keyRules) {
                    if(rule.isRule(s)) {
                        isCustomRule = true;
                        if(!rule.isRuleMet(value)) {
                            Log.i(TAG, "Custom rule " + s + " was not satisfied..");
                            return;
                        } else {
                            Log.i(TAG, "Custom rule " + s + " was satisfied..");
                            break;
                        }
                    }
                }
                if(!isCustomRule) {
                    OptionalRules optional = OptionalRules.valueOfName(s);
                    if (optional != OptionalRules.UNKNOWN) {
                        if (!OptionalRules.isOptionalSatisfied(getContext(), optional, value)) {
                            Log.i(TAG, "Rule " + s + " was not satisfied..");
                            return;
                        } else
                            Log.i(TAG, "Rule " + s + " was satisfied..");
                    } else
                        Log.i(TAG, "Rule " + s + " is unknown, ignoring rule..");
                }
            }

            if(previouslyDisplayed(data)) {
                Log.i(TAG, "Notification has been previously shown, won't show again..");
                return;
            }

            Promo promo = Promo.createPromo(title, body, data);
            if(promo != null) {
                Log.i(TAG, "Has promo..");
                PromoManager.savePromo(getContext(), promo);
            }

            FBNotificationManager.clearCachedNotification(context);
            if(!isSilentNotification(data)) {
                displayNotification(title, body, data);
            } else {
                if(cacheSilentNotification(data))
                    FBNotificationManager.cacheNotification(context, title, body, data);
                Log.d(TAG, "Not showing notification, is silent..");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean cacheSilentNotification(Map<String, String> data) {
        if(data.containsKey(FirebaseNotification.Key.CACHE_SILENT.value())) {
            String isSilent = data.get(FirebaseNotification.Key.CACHE_SILENT.value());
            try {
                return Boolean.valueOf(isSilent);
            } catch (Exception e) {
                Log.e(TAG, FirebaseNotification.Key.CACHE_SILENT.value() + " key couldn't be parsed as a boolean..");
            }
        }
        return false;
    }

    private boolean isSilentNotification(Map<String, String> data) {
        if(data.containsKey(FirebaseNotification.Key.IS_SILENT.value())) {
            String isSilent = data.get(FirebaseNotification.Key.IS_SILENT.value());
            try {
                return Boolean.valueOf(isSilent);
            } catch (Exception e) {
                Log.e(TAG, FirebaseNotification.Key.IS_SILENT.value() + " key couldn't be parsed as a boolean..");
            }
        }
        return false;
    }

    private boolean previouslyDisplayed(Map<String, String> data) {
        if(data.containsKey(Promo.Key.ID.value())){
            if(PrefUtils.readBooleanPref(getContext(), data.get(Promo.Key.ID.value())))
                return true;
            PrefUtils.writeBoolPref(getContext(), data.get(Promo.Key.ID.value()), true);
        }
        return false;
    }

    private void displayNotification(String title, String body, Map<String, String> data) {
        FirebaseNotification n = FirebaseNotification.parseNotification(getContext(), title, body, defaultIcon, data, pendingIntentCreator);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), channelId)
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setCategory(NotificationCompat.CATEGORY_PROMO);

        mBuilder.setSmallIcon(n.iconResId)
                .setContentTitle(n.title)
                .setContentText(n.body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(n.body));

        if(n.pendingIntent != null)
            mBuilder.setContentIntent(n.pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID_FIREBASE, mBuilder.build());
    }

    public interface PendingIntentCreator {
        PendingIntent createPendingIntent(Context context, Map<String, String> data);
    }

    public static class Builder {

        private final Context context;
        private String channelId;
        @DrawableRes
        private int defaultIcon;
        private final List<Rule> rules = new ArrayList();
        private final List<KeyRule> keyRules = new ArrayList();
        private PendingIntentCreator pendingIntentCreator;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setChanngelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder setDefaultIcon(@DrawableRes int defaultIcon) {
            this.defaultIcon = defaultIcon;
            return this;
        }

        public Builder addRule(Rule rule) {
            rules.add(rule);
            return this;
        }

        public Builder addKeyRule(KeyRule rule) {
            keyRules.add(rule);
            return this;
        }

        public Builder setPendingIntentCreator(PendingIntentCreator pendingIntentCreator) {
            this.pendingIntentCreator = pendingIntentCreator;
            return this;
        }

        public PromoHandler build() {
            return new PromoHandler(this);
        }

    }

}
