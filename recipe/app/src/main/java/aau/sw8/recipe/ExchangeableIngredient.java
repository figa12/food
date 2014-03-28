package aau.sw8.recipe;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class ExchangeableIngredient {
    private int exchangeableIngredientId;
    private String name;
    private Recipe recipe;
    private ArrayList<Quantity> ingredientList = new ArrayList<>();
    private IngredientGroup ingredientGroup;
    private int order;
    private boolean mandatory;


    public ExchangeableIngredient(int exchangeableIngredientId, String name, Recipe recipe,
                                  IngredientGroup ingredientGroup, int order, boolean mandatory) {
        this.exchangeableIngredientId = exchangeableIngredientId;
        this.name = name;
        this.recipe = recipe;
        this.ingredientList = new ArrayList<Quantity>();
        this.ingredientGroup = ingredientGroup;
        this.order = order;
        this.mandatory = mandatory;
    }

    public ExchangeableIngredient(int exchangeableIngredientId, String name, Recipe recipe, ArrayList<Quantity> ingredientList,
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

    public ArrayList<Quantity> getIngredientList() {
        return ingredientList;
    }
}
