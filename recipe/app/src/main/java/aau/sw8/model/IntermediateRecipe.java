package aau.sw8.model;

/**
 * Created by Jesper on 0028 28. apr.
 */
public class IntermediateRecipe {
    private long id;
    private String name;
    private String description;
    private String image;
    private String missingIngredients;

    public IntermediateRecipe(long id, String name, String description, String image, String missingIngredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.missingIngredients = missingIngredients;
    }

    public IntermediateRecipe(long id, String name, String description, String image) {
        this(id, name, description, image, "");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getMissingIngredients() {
        return missingIngredients;
    }
}
