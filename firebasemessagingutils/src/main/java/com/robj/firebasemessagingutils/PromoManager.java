package com.robj.firebasemessagingutils;

import android.content.Context;
import android.text.TextUtils;

import io.reactivex.Observable;

/**
 * Created by jj on 19/10/17.
 */

public class PromoManager {

    private static final String PROMO_ID = "promoId";
    private static final String PROMO_TITLE = "promoTitle";
    private static final String PROMO_BODY = "promoBody";
    private static final String PROMO_RECEIVED = "PROMO_RECEIVED";
    private static final String PROMO_TTL = "promoTtl";
    private static final String PROMO_CODE = "promoCode";
    private static final String PROMO_THEME = "promoTheme";

    private static Promo getPromo(Context context) {
        long id = PrefUtils.readLongPref(context, PROMO_ID);
        String title = PrefUtils.readStringPref(context, PROMO_TITLE);
        String body = PrefUtils.readStringPref(context, PROMO_BODY);
        long received = PrefUtils.readLongPref(context, PROMO_RECEIVED);
        int ttl = PrefUtils.readIntPref(context, PROMO_TTL);
        int code = PrefUtils.readIntPref(context, PROMO_CODE);
        String theme = PrefUtils.readStringPref(context, PROMO_THEME);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body) && received != 0 && ttl > 0 && code > 0)
            return new Promo(id, title, body, received, ttl, code, theme);
        return null;
    }

    public static void savePromo(Context context, Promo promo) {
        PrefUtils.writeLongPref(context, PROMO_ID, promo.id);
        PrefUtils.writeStringPref(context, PROMO_TITLE, promo.title);
        PrefUtils.writeStringPref(context, PROMO_BODY, promo.body);
        PrefUtils.writeLongPref(context, PROMO_RECEIVED, promo.dateReceived);
        PrefUtils.writeIntPref(context, PROMO_TTL, promo.timeToLive);
        PrefUtils.writeIntPref(context, PROMO_CODE, promo.code);
        PrefUtils.writeStringPref(context, PROMO_THEME, promo.theme);
    }

    public static void clearPromo(Context context) {
        String[] prefs = new String[] { PROMO_TITLE, PROMO_BODY, PROMO_RECEIVED, PROMO_TTL, PROMO_CODE, PROMO_THEME };
        PrefUtils.removePrefs(context, prefs);
    }

    public static Observable<Optional<Promo>> getActivePromo(Context context) {
        return Observable.create(e -> {
            Promo promo = getPromo(context);
            if(promo != null && promo.isExpired()) {
                clearPromo(context);
                promo = null;
            }
            e.onNext(new Optional(promo));
            e.onComplete();
            return;
        });
    }
}
