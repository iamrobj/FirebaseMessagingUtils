package com.robj.firebasemessagingutils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jj on 19/10/17.
 */
public class Promo implements Parcelable {

    private static final String OFFER_CODE = "offer_code";
    private static final String TTL = "ttl";
    private static final String THEME = "theme";
    public static final String ID = "id";

    public final long id;
    public final String title;
    public final String body;
    public final long dateReceived;
    public final int timeToLive; //in hours
    public final int code;
    public final String theme;

    public Promo(long id, String title, String body, long dateReceived, int timeToLive, int code, String theme) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.dateReceived = dateReceived;
        this.timeToLive = timeToLive;
        this.code = code;
        this.theme = theme;
    }

    protected Promo(Parcel in) {
        id = in.readLong();
        title = in.readString();
        body = in.readString();
        dateReceived = in.readLong();
        timeToLive = in.readInt();
        code = in.readInt();
        theme = in.readString();
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeLong(dateReceived);
        dest.writeInt(timeToLive);
        dest.writeInt(code);
        dest.writeString(theme);
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis();
        long diff = now - dateReceived;
        long hoursBetween = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        return hoursBetween > timeToLive;
    }

    public static Promo createPromo(String title, String body, Map<String, String> data) {
        if(data.containsKey(OFFER_CODE) && data.containsKey(TTL)) {
            int code = Integer.parseInt(data.get(OFFER_CODE));
            int ttl = Integer.parseInt(data.get(TTL));
            long id = data.containsKey(ID) ? Long.parseLong(data.get(ID)) : 0;
            String theme = data.get(THEME);
            return new Promo(id, title, body, Calendar.getInstance().getTimeInMillis(), ttl, code, theme);
        }
        return null;
    }

}
