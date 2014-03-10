package aau.sw8.recipe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeria_000 on 07-03-14.
 */
public class Recipe implements Parcelable {

    private String imagePath;
    private String recipeTitle;
    private String recipeDescription;

    public Recipe(String imagePath, String recipeTitle, String recipeDescription) {
        this.imagePath = imagePath;
        this.recipeTitle = recipeTitle;
        this.recipeDescription = recipeDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.imagePath);
        out.writeString(this.recipeTitle);
        out.writeString(this.recipeDescription);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        this.imagePath = in.readString();
        this.recipeTitle = in.readString();
        this.recipeDescription = in.readString();
    }
}
