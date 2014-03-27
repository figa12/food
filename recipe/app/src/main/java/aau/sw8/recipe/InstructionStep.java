package aau.sw8.recipe;

/**
 * Created by jeria_000 on 0026 26. mar.
 */
public class InstructionStep {

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
}
