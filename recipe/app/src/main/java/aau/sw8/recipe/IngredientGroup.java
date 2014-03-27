package aau.sw8.recipe;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientGroup {
    private int ingredientGroupId;
    private String name;
    private int order;
    private Recipe recipe;

    public IngredientGroup(int ingredientGroupId, String name, int order, Recipe recipe) {
        this.ingredientGroupId = ingredientGroupId;
        this.name = name;
        this.order = order;
        this.recipe = recipe;
    }

    public int getId() {
        return ingredientGroupId;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
