package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeria_000 on 0026 26. mar.
 */
public class InstructionStep implements Parcelable {

    private String description;
    private int step;
    private String imagePath;

    public InstructionStep(String description, int step, String imageUrl) {
        this.description = description;
        this.imagePath = imageUrl;
        this.step = step;
    }

    public InstructionStep(String description, int step) {
        this(description, step, null);
    }

    public String getDescription() {
        return description;
    }

    public int getStep() {
        return step;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.description);
        out.writeInt(this.step);
        out.writeString(this.imagePath);
    }

    public static final Parcelable.Creator<InstructionStep> CREATOR = new Parcelable.Creator<InstructionStep>() {
        @Override
        public InstructionStep createFromParcel(Parcel in) {
            return new InstructionStep(in);
        }

        @Override
        public InstructionStep[] newArray(int size) {
            return new InstructionStep[size];
        }
    };

    private InstructionStep(Parcel in) {
        this.description = in.readString();
        this.step = in.readInt();
        this.imagePath = in.readString();
    }
}
