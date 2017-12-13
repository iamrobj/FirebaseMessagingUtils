package com.robj.firebasemessagingutils;

import android.text.TextUtils;

/**
 * Created by jj on 26/11/17.
 */

public abstract class KeyRule {

    private final String key;

    public KeyRule(String key) {
        if(TextUtils.isEmpty(key))
            throw new RuntimeException("KeyRule key cannot be empty!");
        this.key = key;
    }

    protected final boolean isRule(String key) {
        return this.key.equals(key);
    }

    public abstract boolean isRuleMet(String value);
}
