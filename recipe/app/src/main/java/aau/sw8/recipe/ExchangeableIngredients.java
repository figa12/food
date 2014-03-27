package aau.sw8.recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 3/27/14.
 */
public class ExchangeableIngredients {
    private int exchangeableIngredientId;
    private String name;
    private Recipe recipe;
    private List<Quantity> ingredientList;
    private IngredientGroup ingredientGroup;
    private int order;
    private boolean mandatory;


    public ExchangeableIngredients(int exchangeableIngredientId, String name, Recipe recipe,
                                   IngredientGroup ingredientGroup, int order, boolean mandatory) {
        this.exchangeableIngredientId = exchangeableIngredientId;
        this.name = name;
        this.recipe = recipe;
        this.ingredientList = new ArrayList<Quantity>();
        this.ingredientGroup = ingredientGroup;
        this.order = order;
        this.mandatory = mandatory;
    }

    public ExchangeableIngredients(int exchangeableIngredientId, String name, Recipe recipe, List<Quantity> ingredientList,
                                   IngredientGroup ingredientGroup, int order, boolean mandatory) {
        this.exchangeableIngredientId = exchangeableIngredientId;
        this.name = name;
        this.recipe = recipe;
        this.ingredientList = ingredientList;
        this.ingredientGroup = ingredientGroup;
        this.order = order;
        this.mandatory = mandatory;
    }

    public int getId() {
        return exchangeableIngredientId;
    }

    public String getName() {
        return name;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public IngredientGroup getIngredientGroup() {
        return ingredientGroup;
    }

    public int getOrder() {
        return order;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void addIngredient(Quantity quantity){
        this.ingredientList.add(quantity);
    }
}
