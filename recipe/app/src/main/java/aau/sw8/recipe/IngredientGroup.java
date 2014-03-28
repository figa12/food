package aau.sw8.recipe;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientGroup {
    private int ingredientGroupId;
    private String name;
    private int order;
    private Recipe recipe;
    private ArrayList<ExchangeableIngredient> exchangeableIngredients = new ArrayList<>();

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

    public ArrayList<ExchangeableIngredient> getExchangeableIngredients() {
        return exchangeableIngredients;
    }

    public void setExchangeableIngredients(ArrayList<ExchangeableIngredient> exchangeableIngredients) {
        this.exchangeableIngredients = exchangeableIngredients;
    }
}
