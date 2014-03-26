package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

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
    protected void onClick(View view) {
        // Flash the image
        flashView(view);

        // The tag is the recipe which was clicked
        Recipe recipe = (Recipe) view.getTag();

        // Open recipe fragment
        ((MainActivity) this.getContext()).openRecipeFragment(recipe);
    }
}
