package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import aau.sw8.model.ExchangeableIngredient;
import aau.sw8.model.Quantity;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeIngredientGroupList extends ListLinearLayout<ExchangeableIngredient> {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    public RecipeIngredientGroupList(Context context) {
        super(context);
    }

    public RecipeIngredientGroupList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected View makeView(ExchangeableIngredient exchangeableIngredient) {
        // Get inflater and inflate item
        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ingredientView = layoutInflater.inflate(R.layout.recipe_ingredient_item, null);

        Button shoppingListButton = (Button) ingredientView.findViewById(R.id.recipeShoppingListButton);
        shoppingListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add/remove in shopping list
            }
        });

        Spinner spinner = (Spinner) ingredientView.findViewById(R.id.exchangeableIngredientSpinner);
        ArrayList<Quantity> ingredientQuantity = exchangeableIngredient.getIngredientList();
        //ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, /* List of strings I think */);
        //spinner.setAdapter(ingredientAdapter);

        return ingredientView;
    }
}
