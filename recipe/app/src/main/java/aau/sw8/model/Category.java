package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class Category implements Parcelable{
    private long categoryId;
    private String name;
    private Category parent;
    private String imagepath;

    public Category(long categoryId, String name, Category parent, String imagepath) {
        this.categoryId = categoryId;
        this.name = name;
        this.parent = parent;
        this.imagepath = imagepath;
    }

    public long getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public String getImagepath() {
        return imagepath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.categoryId);
        out.writeString(this.name);
        out.writeString(this.imagepath);
        // cannot save category parent, would result in stackoverflow
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private Category(Parcel in) {
        this.categoryId = in.readLong();
        this.name = in.readString();
        this.imagepath = in.readString();
    }
}
