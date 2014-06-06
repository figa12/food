package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jesper on 07-03-14.
 */
public class Recipe extends IntermediateRecipe implements Parcelable {

    private String description;
    private ArrayList<IngredientGroup> ingredientGroups = new ArrayList<>();
    private ArrayList<InstructionStep> instructionSteps = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private long upvotes;
    private long downvotes;

    private String source;
    private String licenseName;
    private String licenseLink;
    private String licenseNote;

    public Recipe(long id, String image, String name, String recipeDescription, ArrayList<IngredientGroup> ingredientGroups, ArrayList<InstructionStep> instructionSteps, long upvotes, long downvotes, String source, String licenseName, String licenseLink, String licenseNote) {
        super(id, name, image, "");

        this.description = recipeDescription;
        this.ingredientGroups = ingredientGroups;
        this.instructionSteps = instructionSteps;

        this.upvotes = upvotes;
        this.downvotes = downvotes;

        this.source = source;
        this.licenseName = licenseName;
        this.licenseLink = licenseLink;
        this.licenseNote = licenseNote;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<InstructionStep> getInstructionSteps() {
        return instructionSteps;
    }

    public ArrayList<IngredientGroup> getIngredientGroups() {
        return ingredientGroups;
    }

    public long getUpvotes() {
        return this.upvotes;
    }

    public long getDownvotes() {
        return this.downvotes;
    }

    public String getLicenseHtml() {
        return this.source + "<br><a href=\"" + this.licenseLink + "\">" + this.licenseName + "</a>" + (!this.licenseNote.equals("") ? "<br>" + this.licenseNote : "");
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
        out.writeString(this.source);
        out.writeString(this.licenseName);
        out.writeString(this.licenseLink);
        out.writeString(this.licenseNote);
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
        this.source = in.readString();
        this.licenseName = in.readString();
        this.licenseLink = in.readString();
        this.licenseNote = in.readString();
    }
}
