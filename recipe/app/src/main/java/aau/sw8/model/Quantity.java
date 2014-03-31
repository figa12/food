package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class Quantity  {
    private int quantityId;
    private Ingredient ingredient;
    private Unit unit;
    private double amount;

    public Quantity(int quantityId, Ingredient ingredient, Unit unit, double amount) {
        this.quantityId = quantityId;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    public int getId() {
        return quantityId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Unit getUnit() {
        return unit;
    }

    public double getAmount() {
        return amount;
    }
}
