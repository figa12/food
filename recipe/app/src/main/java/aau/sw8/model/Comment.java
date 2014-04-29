package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class Comment implements Parcelable {
    private long commentId;
    private User user;
    private String text;

    public Comment(long commentId, User user, String text) {
        this.commentId = commentId;
        this.user = user;
        this.text = text;
    }

    public long getId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(commentId);
        out.writeParcelable(this.user,PARCELABLE_WRITE_RETURN_VALUE);
        out.writeString(this.text);
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    private Comment(Parcel in) {
        this.commentId = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.text = in.readString();
    }
}
