package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import aau.sw8.model.ExchangeableIngredient;

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
        this.setOrientation(VERTICAL);
    }

    public RecipeIngredientGroupList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
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


        ArrayList<String> str = exchangeableIngredient.getExchangeableIngredientStrings();

        if (str.size() > 1) {
            //TODO make actual exchangeable ingredient adapter
            Spinner spinner = (Spinner) ingredientView.findViewById(R.id.exchangeableIngredientSpinner);
            spinner.setVisibility(VISIBLE);

            ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, str);
            ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(ingredientAdapter);
        } else if (str.size() == 1) {
            TextView textView = (TextView) ingredientView.findViewById(R.id.exchangeableIngredientTextView);
            textView.setVisibility(VISIBLE);
            textView.setText(str.get(0));
        }

        return ingredientView;
    }
}
