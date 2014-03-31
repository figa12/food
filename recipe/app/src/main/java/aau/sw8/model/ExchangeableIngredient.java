package aau.sw8.model;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class ExchangeableIngredient {

    private int exchangeableIngredientId;
    private String name;
    private ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
    private boolean mandatory;

    public ExchangeableIngredient(int exchangeableIngredientId, String name,
                                  ArrayList<IngredientQuantity> ingredientQuantities, boolean mandatory) {
        this.exchangeableIngredientId = exchangeableIngredientId;
        this.name = name;
        this.ingredientQuantities = ingredientQuantities;
        this.mandatory = mandatory;
    }

    public int getId() {
        return exchangeableIngredientId;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public ArrayList<IngredientQuantity> getIngredientQuantities() {
        return ingredientQuantities;
    }

    public ArrayList<String> getExchangeableIngredientStrings() {
        ArrayList<String> strings = new ArrayList<>();

        for (IngredientQuantity ingredientQuantity : this.ingredientQuantities) {
            strings.add(ingredientQuantity.getString());
        }

        return strings;
    }
}
