package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import aau.sw8.model.IntermediateRecipe;

/**
 * Created by Sam on 26/03/2014.
 */
public class SearchList extends RecipeList {

    public SearchList(Context context) {
        super(context);
    }

    public SearchList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View makeView(IntermediateRecipe intermediateRecipe) {
        View recipeView = super.makeView(intermediateRecipe);

        TextView notification = (TextView) recipeView.findViewById(R.id.notification);
        notification.setText(intermediateRecipe.getMissingIngredients());

        TextView rating = (TextView) recipeView.findViewById(R.id.rating);
        for (int i = random.nextInt(5); i != 5; ++i) {
            rating.append("★");
        }

        return recipeView;
    }

    private Random random = new Random();
}
