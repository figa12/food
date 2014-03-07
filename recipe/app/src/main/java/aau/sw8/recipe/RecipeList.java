package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeList extends ListLinearLayout<Recipe>{

    public RecipeList(Context context) {
        super(context);
    }

    public RecipeList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected View makeView(Recipe recipe) {
        // Get inflater and inflate item
        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View recipeView = layoutInflater.inflate(R.layout.recipe_list_item, null);

        // Set recipe image
        ImageView recipeImage = (ImageView) recipeView.findViewById(R.id.recipeImageView);
        // TODO: use imageloader to set image

        // Set recipe title
        TextView recipeTitle = (TextView) recipeView.findViewById(R.id.titleTextView);
        recipeTitle.setText(recipe.getRecipeTitle());

        // Set recipe description
        TextView recipeDescription = (TextView) recipeView.findViewById(R.id.descriptionTextView);
        recipeDescription.setText(recipe.getRecipeDescription());

        return recipeView;
    }
}
