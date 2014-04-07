package aau.sw8.model;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientQuantity {
    private int quantityId;
    private Ingredient ingredient;
    private Unit unit;
    private double amount;

    public IngredientQuantity(int quantityId, Ingredient ingredient, Unit unit, double amount) {
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

    public String getString() {
        String string;

        // if the amount is an integer, convert it
        if (this.amount == Math.abs(this.amount))
            string = String.valueOf((int) this.amount);
        else
            string = String.valueOf(this.amount);

        // it may be an unit-less ingredient
        if (this.unit != null)
            string += " " + this.unit.getMetric() + " ";
        else
            string += " ";

        if (this.amount != 1.0) {
            string += this.ingredient.getPlural();
        } else {
            string += this.ingredient.getSingular();
        }

        return string;
    }
}
