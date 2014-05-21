package aau.sw8.model;

/**
 * Created by Jesper on 0028 28. apr.
 */
public class IntermediateRecipe {
    protected long id;
    protected String name;
    protected String image;
    protected String missingIngredients;

    public IntermediateRecipe(long id, String name, String image, String missingIngredients) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.missingIngredients = missingIngredients;
    }

    public IntermediateRecipe(long id, String name, String image) {
        this(id, name, image, "");
    }

    protected IntermediateRecipe() {
        // used for Recipe parcelable
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMissingIngredients() {
        return missingIngredients;
    }
}
