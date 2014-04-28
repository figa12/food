package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class User implements Parcelable{
    private long userId;
    private String token;
    private String username;

    public User(long userId, String token, String username) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.userId);
        out.writeString(this.token);
        out.writeString(this.username);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        this.userId = in.readLong();
        this.token = in.readString();
        this.username = in.readString();
    }
}
