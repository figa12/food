package aau.sw8.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jeria_000 on 07-03-14.
 */
public class Recipe implements Parcelable {

    private int recipeId;
    private String imagePath;
    private String recipeTitle;
    private String recipeDescription;
    private ArrayList<IngredientGroup> ingredient = new ArrayList<>();
    private ArrayList<InstructionStep> instructionSteps = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    /*Constructors*/
    public Recipe(String imagePath, String recipeTitle) {
        this.imagePath = imagePath;
        this.recipeTitle = recipeTitle;

        this.comments.add(new Comment(0, new User(0,0,"Ramin Sadre"),this, "mmmm, so yummy in my tummy"));
    }


    /*Methods*/
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

    public ArrayList<InstructionStep> getInstructionSteps() {
        return instructionSteps;
    }

    public void setInstructionSteps(ArrayList<InstructionStep> instructionSteps) {
        this.instructionSteps = instructionSteps;
    }

    public ArrayList<IngredientGroup> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<IngredientGroup> ingredient) {
        this.ingredient = ingredient;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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
