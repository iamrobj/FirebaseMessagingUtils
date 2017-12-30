package com.robj.firebasemessagingutils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jj on 20/10/17.
 */
enum OptionalRules {

    MIN_VERSION_CODE, VERSION_CODE, UNKNOWN;

    private static final Map<String, OptionalRules> map = new HashMap();
    private static final String TAG = OptionalRules.class.getSimpleName();

    static {
        for (OptionalRules optional : OptionalRules.values())
            map.put(optional.name().toLowerCase(), optional);
    }

    public static OptionalRules valueOfName(final String name) {
        OptionalRules optional = map.get(name.toLowerCase());
        if (optional == null)
            optional = OptionalRules.UNKNOWN;
        return optional;
    }

    public static boolean isOptionalSatisfied(Context context, OptionalRules optional, String key) {
        switch (optional) {
            case MIN_VERSION_CODE:
                try {
                    int currentVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                    int minVersionCode = Integer.valueOf(key);
                    return currentVersionCode >= minVersionCode;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            case VERSION_CODE:
                try {
                    int currentVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                    int versionCode = Integer.valueOf(key);
                    return currentVersionCode == versionCode;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            default:
                Log.i(TAG, "Key with value " + key + " is unknown, ignoring rule..");
                return true; //Ignore new keys
        }
    }

}
