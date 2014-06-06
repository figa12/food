package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientQuantity implements Parcelable {
    private long quantityId;
    private Ingredient ingredient;
    private Unit unit;
    private double amount;

    public IngredientQuantity(long quantityId, Ingredient ingredient, Unit unit, double amount) {
        this.quantityId = quantityId;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    public long getId() {
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

    public String getDescription() {
        String string = "";

        // if the amount is an integer, cast it
        if (this.amount != 0.0) {
            if (this.amount == Math.floor(this.amount))
                string += String.valueOf((int) this.amount);
            else
                string += String.valueOf(this.amount);
        }

        // it may be a unit-less ingredient
        if (this.unit != null)
            string += " " + this.unit.getMetric() + " ";
        else
            string += " ";

        if (this.unit == null && this.amount == 1.0) {
            string += this.ingredient.getSingular();
        } else {
            string += this.ingredient.getPlural();
        }

        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.quantityId);
        out.writeParcelable(this.ingredient, PARCELABLE_WRITE_RETURN_VALUE);
        out.writeParcelable(this.unit, PARCELABLE_WRITE_RETURN_VALUE);
        out.writeDouble(this.amount);
    }

    public static final Parcelable.Creator<IngredientQuantity> CREATOR = new Parcelable.Creator<IngredientQuantity>() {
        @Override
        public IngredientQuantity createFromParcel(Parcel in) {
            return new IngredientQuantity(in);
        }

        @Override
        public IngredientQuantity[] newArray(int size) {
            return new IngredientQuantity[size];
        }
    };

    private IngredientQuantity(Parcel in) {
        this.quantityId = in.readLong();
        this.ingredient = in.readParcelable(Ingredient.class.getClassLoader());
        this.unit = in.readParcelable(Unit.class.getClassLoader());
        this.amount = in.readDouble();
    }
}
