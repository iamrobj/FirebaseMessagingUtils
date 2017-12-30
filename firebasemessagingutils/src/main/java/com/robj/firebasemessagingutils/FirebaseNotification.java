package com.robj.firebasemessagingutils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

/**
 * Created by jj on 20/10/17.
 */
public class FirebaseNotification {

    private static final String TAG = FirebaseNotification.class.getSimpleName();

    public final String title;
    public final String body;
    @DrawableRes
    public final int iconResId;
    public final PendingIntent pendingIntent;

    public FirebaseNotification(String title, String body, int iconResId, PendingIntent pendingIntent) {
        this.title = title;
        this.body = body;
        this.iconResId = iconResId;
        this.pendingIntent = pendingIntent;
    }

    public static FirebaseNotification parseNotification(Context context, String title, String body,
                                                         @DrawableRes int defaultIcon, Map<String, String> data, PromoHandler.PendingIntentCreator pendingIntentHandler) {
        String icon = data.get(Key.ICON.value());
        int iconResId = 0;
        if(!TextUtils.isEmpty(icon))
            iconResId = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        if(iconResId == 0)
            iconResId = defaultIcon;
        else
            Log.i(TAG, "Has custom icon " + icon + "..");

        PendingIntent pendingIntent = createPendingIntent(context, data, pendingIntentHandler);

        return new FirebaseNotification(title, body, iconResId, pendingIntent);
    }

    private static PendingIntent createPendingIntent(Context context, Map<String, String> data, PromoHandler.PendingIntentCreator pendingIntentCreator) {
        PendingIntent pendingIntent = null;
        if(pendingIntentCreator != null)
            pendingIntent = pendingIntentCreator.createPendingIntent(context, data);

        if(pendingIntent == null) {
            Intent i = null;
            if (data.containsKey(Key.RATE.value()))
                i = getUrlIntent("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            else if (data.containsKey(Key.LINK_TO.value()))
                i = getUrlIntent(data.get(Key.LINK_TO.value()));
            if (i != null)
                pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return pendingIntent;
    }

    private static Intent getUrlIntent(String url) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_NEW_TASK);
        return marketIntent;
    }

    public enum Key {
        ID, RATE, LINK_TO, ICON, IS_SILENT, CACHE_SILENT;
        public String value() {
            return name().toLowerCase();
        }
    }

}
