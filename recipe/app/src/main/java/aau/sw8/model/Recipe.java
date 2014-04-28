package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jeria_000 on 07-03-14.
 */
public class Recipe implements Parcelable {

    private long recipeId;
    private String imagePath;
    private String recipeTitle;
    private String recipeDescription;
    private ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
    private ArrayList<InstructionStep> instructionSteps = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private long upvotes;
    private long downvotes;

    /*Constructors*/
    public Recipe(String imagePath, String recipeTitle) {
        this.imagePath = imagePath;
        this.recipeTitle = recipeTitle;

        this.comments.add(new Comment(0L, new User(0,"token","Ramin Sadre"), "mmmm, so yummy in my tummy"));
    }

    public Recipe(long recipeId, String imagePath, String recipeTitle, String recipeDescription, ArrayList<IngredientGroup> ingredientGroups, ArrayList<InstructionStep> instructionSteps) {
        this.recipeId = recipeId;
        this.imagePath = imagePath;
        this.recipeTitle = recipeTitle;
        this.recipeDescription = recipeDescription;
        this.ingredientGroups = ingredientGroups;
        this.instructionSteps = instructionSteps;
    }

    public Recipe(long recipeId, String imagePath, String recipeTitle, String recipeDescription, ArrayList<IngredientGroup> ingredientGroups, ArrayList<InstructionStep> instructionSteps, long upvotes, long downvotes) {
        this(recipeId, imagePath, recipeTitle, recipeDescription, ingredientGroups, instructionSteps);
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    /*Methods*/

    public long getRecipeId() {
        return recipeId;
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

    public ArrayList<InstructionStep> getInstructionSteps() {
        return instructionSteps;
    }

    public void setInstructionSteps(ArrayList<InstructionStep> instructionSteps) {
        this.instructionSteps = instructionSteps;
    }

    public ArrayList<IngredientGroup> getIngredientGroups() {
        return ingredientGroups;
    }

    public void setIngredientGroups(ArrayList<IngredientGroup> ingredientGroups) {
        this.ingredientGroups = ingredientGroups;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public long getUpvotes() {
        return this.upvotes;
    }

    public long getDownvotes() {
        return this.downvotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.recipeId);
        out.writeString(this.imagePath);
        out.writeString(this.recipeTitle);
        out.writeString(this.recipeDescription);
        out.writeList(this.ingredientGroups);
        out.writeList(this.instructionSteps);
        out.writeList(this.comments);
        out.writeLong(this.upvotes);
        out.writeLong(this.downvotes);
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
        this.recipeId = in.readLong();
        this.imagePath = in.readString();
        this.recipeTitle = in.readString();
        this.recipeDescription = in.readString();
        in.readList(this.ingredientGroups, IngredientGroup.class.getClassLoader());
        in.readList(this.instructionSteps, InstructionStep.class.getClassLoader());
        in.readList(this.comments, Comment.class.getClassLoader());
        this.upvotes = in.readLong();
        this.downvotes = in.readLong();
    }
}
