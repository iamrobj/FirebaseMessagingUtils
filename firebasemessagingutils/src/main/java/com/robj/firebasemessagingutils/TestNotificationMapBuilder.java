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
        map.put(Promo.Key.ID.value(), String.valueOf(id));
        return this;
    }

    public TestNotificationMapBuilder setPromo(TestPromoMapBuilder promoMapBuilder) {
        map.putAll(promoMapBuilder.build());
        return this;
    }

    public TestNotificationMapBuilder setIsSilent(boolean isSilent) {
        map.put(FirebaseNotification.Key.IS_SILENT.value(), String.valueOf(isSilent));
        return this;
    }

    public TestNotificationMapBuilder shouldCacheIfSilent(boolean shouldCache) {
        map.put(FirebaseNotification.Key.CACHE_SILENT.value(), String.valueOf(shouldCache));
        return this;
    }

    public TestNotificationMapBuilder setAskForRating(boolean askForRating) {
        if(map.containsKey(FirebaseNotification.Key.LINK_TO.value()))
            throw new RuntimeException("Ask for rating will override url link provided..");
        map.put(FirebaseNotification.Key.RATE.value(), String.valueOf(askForRating));
        return this;
    }

    public TestNotificationMapBuilder setLinkTo(String urlToOpen) {
        if(map.containsKey(FirebaseNotification.Key.RATE.value()))
            throw new RuntimeException("Link to will override ask for rating..");
        map.put(FirebaseNotification.Key.LINK_TO.value(), urlToOpen);
        return this;
    }

    public Map<String, String> build() {
        return map;
    }

}
