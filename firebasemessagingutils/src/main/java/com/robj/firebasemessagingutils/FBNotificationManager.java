package com.robj.firebasemessagingutils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jj on 19/10/17.
 */

public class FBNotificationManager {

    private static final String TAG = FBNotificationManager.class.getSimpleName();
    public static final String TITLE = "title";
    public static final String BODY = "body";

    private static Map<String, String> getCachedNotification(Context context) {
        Map<String, String> data = new HashMap<>();
        data.put(TITLE, PrefUtils.readStringPref(context, TITLE));
        data.put(BODY, PrefUtils.readStringPref(context, BODY));
        for(FirebaseNotification.Key key : FirebaseNotification.Key.values()) {
            String value = PrefUtils.readStringPref(context, key.value());
            if(!TextUtils.isEmpty(value))
                data.put(key.value(), value);
        }
        return data;
    }

    public static void clearCachedNotification(Context context) {
        List<String> keys = new ArrayList<>();
        keys.add(TITLE);
        keys.add(BODY);
        for(FirebaseNotification.Key key : FirebaseNotification.Key.values())
            keys.add(key.value());
        String[] prefs = new String[keys.size()];
        keys.toArray(prefs);
        PrefUtils.removePrefs(context, prefs);
        Log.d(TAG, "Cleared silent notification cache..");
    }

    public static Observable<Optional<Map<String, String>>> getCachedNotificationObservable(Context context) {
        return Observable.create((ObservableOnSubscribe<Optional<Map<String, String>>>) e -> {
            Map<String, String> map = getCachedNotification(context);
            e.onNext(new Optional(map != null && !map.isEmpty() ? map : null));
            e.onComplete();
            return;
        }).subscribeOn(Schedulers.io());
    }

    protected static void cacheNotification(Context context, String title, String body, Map<String, String> data) {
        PrefUtils.writeStringPref(context, TITLE, title);
        PrefUtils.writeStringPref(context, BODY, body);
        for(FirebaseNotification.Key key : FirebaseNotification.Key.values()) {
            if(data.containsKey(key.value()))
                PrefUtils.writeStringPref(context, key.value(), data.get(key.value()));
        }
        Log.d(TAG, "Cached silent notification..");
    }

}
