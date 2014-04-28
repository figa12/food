package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class Ingredient implements Parcelable{
    private long ingredientId;
    private String singular;
    private String plural;
    private Category category;

    public Ingredient(long ingredientId, String singular, String plural, Category category) {
        this.ingredientId = ingredientId;
        this.singular = singular;
        this.plural = plural;
        this.category = category;
    }

    public long getId(){
        return ingredientId;
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.ingredientId);
        out.writeString(this.singular);
        out.writeString(this.plural);
        out.writeParcelable(this.category, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in) {
        this.ingredientId = in.readLong();
        this.singular = in.readString();
        this.plural = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
    }
}
