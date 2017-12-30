package com.robj.firebasemessagingutils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jj on 30/12/17.
 */

public class TestPromoMapBuilder {

    Map<String, String> map = new HashMap<>();

    public TestPromoMapBuilder setId(int id) {
        map.put(Promo.ID, String.valueOf(id));
        return this;
    }

    public TestPromoMapBuilder setTimeToLive(int ttl) {
        map.put(Promo.TTL, String.valueOf(ttl));
        return this;
    }

    public TestPromoMapBuilder setOfferCode(int offerCode) {
        map.put(Promo.OFFER_CODE, String.valueOf(offerCode));
        return this;
    }

    public TestPromoMapBuilder setTheme(String themeName) {
        map.put(Promo.THEME, themeName);
        return this;
    }

    public Map<String, String> build() {
        return map;
    }

}
