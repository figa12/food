package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class ExchangeableIngredient implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.exchangeableIngredientId);
        out.writeList(this.ingredientQuantities);
        out.writeValue(this.mandatory); // sigh, WriteBoolean does not exist
    }

    public static final Parcelable.Creator<ExchangeableIngredient> CREATOR = new Parcelable.Creator<ExchangeableIngredient>() {
        @Override
        public ExchangeableIngredient createFromParcel(Parcel in) {
            return new ExchangeableIngredient(in);
        }

        @Override
        public ExchangeableIngredient[] newArray(int size) {
            return new ExchangeableIngredient[size];
        }
    };

    private ExchangeableIngredient(Parcel in) {
        this.exchangeableIngredientId = in.readLong();
        in.readList(this.ingredientQuantities, IngredientQuantity.class.getClassLoader());
        this.mandatory = (boolean) in.readValue(Boolean.class.getClassLoader());
    }
}
