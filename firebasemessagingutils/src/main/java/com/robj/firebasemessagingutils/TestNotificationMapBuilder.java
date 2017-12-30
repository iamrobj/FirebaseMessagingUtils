package com.robj.firebasemessagingutils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jj on 30/12/17.
 */

public class TestNotificationMapBuilder {

    Map<String, String> map = new HashMap<>();

    public TestNotificationMapBuilder setTargetVersionCode(int targetVersionCode) {
        map.put(OptionalRules.VERSION_CODE.name().toLowerCase(), String.valueOf(targetVersionCode));
        return this;
    }

    public TestNotificationMapBuilder setMinVersionCode(int minVersionCode) {
        map.put(OptionalRules.MIN_VERSION_CODE.name().toLowerCase(), String.valueOf(minVersionCode));
        return this;
    }

    public TestNotificationMapBuilder setId(int id) {
        map.put(Promo.ID, String.valueOf(id));
        return this;
    }

    public TestNotificationMapBuilder setTimeToLive(int ttl) {
        map.put(Promo.TTL, String.valueOf(ttl));
        return this;
    }

    public TestNotificationMapBuilder setOfferCode(int offerCode) {
        map.put(Promo.OFFER_CODE, String.valueOf(offerCode));
        return this;
    }

    public TestNotificationMapBuilder setTheme(String themeName) {
        map.put(Promo.THEME, themeName);
        return this;
    }

    public TestNotificationMapBuilder setIsSilent(boolean isSilent) {
        map.put(PromoHandler.IS_SILENT, String.valueOf(isSilent));
        return this;
    }

    public TestNotificationMapBuilder setAskForRating(boolean askForRating) {
        if(map.containsKey(FirebaseNotification.LINK_TO))
            throw new RuntimeException("Ask for rating will override url link provided..");
        map.put(FirebaseNotification.RATE, String.valueOf(askForRating));
        return this;
    }

    public TestNotificationMapBuilder setLinkTo(String urlToOpen) {
        if(map.containsKey(FirebaseNotification.RATE))
            throw new RuntimeException("Link to will override ask for rating..");
        map.put(FirebaseNotification.LINK_TO, urlToOpen);
        return this;
    }

    public Map<String, String> build() {
        return map;
    }

}
