package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jacob on 3/27/14.
 */
public class User implements Parcelable{
    private String hash;
    private String token;
    private String personName;

    public User(String personName, String hash, String token){
        this.personName = personName;
        this.hash = hash;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public static String doHash(String email) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(email.getBytes());
            sha256.reset();

            byte[] hashBytes = sha256.digest();

            StringBuffer hashString = new StringBuffer();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            Log.i("hash", hashString.toString());
            return hashString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException: " + e.getMessage());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.personName);
        out.writeString(this.hash);
        out.writeString(this.token);
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
        this.personName = in.readString();
        this.hash = in.readString();
        this.token = in.readString();
    }
}
