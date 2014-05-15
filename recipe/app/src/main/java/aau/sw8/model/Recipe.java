package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jesper on 07-03-14.
 */
public class Recipe extends IntermediateRecipe implements Parcelable {

    private ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
    private ArrayList<InstructionStep> instructionSteps = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private long upvotes;
    private long downvotes;

    public Recipe(long id, String image, String name, String recipeDescription, ArrayList<IngredientGroup> ingredientGroups, ArrayList<InstructionStep> instructionSteps) {
        super(id, name, recipeDescription, image, "");

        this.ingredientGroups = ingredientGroups;
        this.instructionSteps = instructionSteps;
    }

    public Recipe(long id, String image, String name, String recipeDescription, ArrayList<IngredientGroup> ingredientGroups, ArrayList<InstructionStep> instructionSteps, long upvotes, long downvotes) {
        this(id, image, name, recipeDescription, ingredientGroups, instructionSteps);
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public ArrayList<InstructionStep> getInstructionSteps() {
        return instructionSteps;
    }

    public ArrayList<IngredientGroup> getIngredientGroups() {
        return ingredientGroups;
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
        out.writeLong(this.id);
        out.writeString(this.image);
        out.writeString(this.name);
        out.writeString(this.description);
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
        this.id = in.readLong();
        this.image = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        in.readList(this.ingredientGroups, IngredientGroup.class.getClassLoader());
        in.readList(this.instructionSteps, InstructionStep.class.getClassLoader());
        in.readList(this.comments, Comment.class.getClassLoader());
        this.upvotes = in.readLong();
        this.downvotes = in.readLong();
    }
}
