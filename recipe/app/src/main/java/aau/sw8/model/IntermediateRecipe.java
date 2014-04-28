package aau.sw8.model;

import java.util.ArrayList;

/**
 * Created by Jesper on 0028 28. apr.
 */
public class IntermediateRecipe {
    private long id;
    private String name;
    private String description;
    private String image;
    private ArrayList<Long> missingIngredientIds;

    public IntermediateRecipe(long id, String name, String description, String image, ArrayList<Long> missingIngredientIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.missingIngredientIds = missingIngredientIds;
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

    public ArrayList<Long> getMissingIngredientIds() {
        return missingIngredientIds;
    }
}
