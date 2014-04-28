package aau.sw8.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jacob on 3/27/14.
 */
public class IngredientGroup implements Parcelable {
    private long ingredientGroupId;
    private String name;
    private ArrayList<ExchangeableIngredient> exchangeableIngredients;

    public IngredientGroup(long ingredientGroupId, String name, ArrayList<ExchangeableIngredient> exchangeableIngredients) {
        this.ingredientGroupId = ingredientGroupId;
        this.name = name;
        this.exchangeableIngredients = exchangeableIngredients;
    }

    public long getId() {
        return ingredientGroupId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ExchangeableIngredient> getExchangeableIngredients() {
        return exchangeableIngredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.ingredientGroupId);
        out.writeString(this.name);
        out.writeList(this.exchangeableIngredients);
    }

    public static final Parcelable.Creator<IngredientGroup> CREATOR = new Parcelable.Creator<IngredientGroup>() {
        @Override
        public IngredientGroup createFromParcel(Parcel in) {
            return new IngredientGroup(in);
        }

        @Override
        public IngredientGroup[] newArray(int size) {
            return new IngredientGroup[size];
        }
    };

    private IngredientGroup(Parcel in) {
        this.ingredientGroupId = in.readLong();
        this.name = in.readString();
        in.readList(this.exchangeableIngredients, ExchangeableIngredient.class.getClassLoader());
    }
}
