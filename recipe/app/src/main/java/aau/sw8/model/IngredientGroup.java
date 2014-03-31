package aau.sw8.model;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientGroup {
    private int ingredientGroupId;
    private String name;
    private ArrayList<ExchangeableIngredient> exchangeableIngredients;

    public IngredientGroup(int ingredientGroupId, String name, ArrayList<ExchangeableIngredient> exchangeableIngredients) {
        this.ingredientGroupId = ingredientGroupId;
        this.name = name;
        this.exchangeableIngredients = exchangeableIngredients;
    }

    public int getId() {
        return ingredientGroupId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ExchangeableIngredient> getExchangeableIngredients() {
        return exchangeableIngredients;
    }
}
