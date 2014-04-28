package aau.sw8.model;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class ExchangeableIngredient {

    private long exchangeableIngredientId;
    private ArrayList<IngredientQuantity> ingredientQuantities = new ArrayList<>();
    private boolean mandatory;

    public ExchangeableIngredient(long exchangeableIngredientId, ArrayList<IngredientQuantity> ingredientQuantities, boolean mandatory) {
        this.exchangeableIngredientId = exchangeableIngredientId;
        this.ingredientQuantities = ingredientQuantities;
        this.mandatory = mandatory;
    }

    public long getId() {
        return exchangeableIngredientId;
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
