package aau.sw8.recipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeList extends ListLinearLayout<Recipe>{

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

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
        this.imageLoader.displayImage(recipe.getImagePath(), recipeImage, this.imageLoaderOptions);

        // Set recipe title
        TextView recipeTitle = (TextView) recipeView.findViewById(R.id.titleTextView);
        recipeTitle.setText(recipe.getRecipeTitle());

        // Set recipe description
        TextView recipeDescription = (TextView) recipeView.findViewById(R.id.descriptionTextView);
        recipeDescription.setText(recipe.getRecipeDescription());

        // Find the clickable part of the item layout and add the recipe as a tag
        View clickableLayout = recipeView.findViewById(R.id.mainLayout);
        clickableLayout.setTag(recipe);
        // .. and add the click listener to it
        clickableLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // The tag is the recipe which was clicked
                Recipe recipe = (Recipe) view.getTag();

                // Open recipe fragment
                ((MainActivity) RecipeList.this.getContext()).openRecipeFragment(recipe);
            }
        });

        return recipeView;
    }

    private class RecipeClickListener implements OnClickListener {
        private Recipe recipe;

        public RecipeClickListener(Recipe recipe) {
            this.recipe = recipe;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClick(View v) {
            // TOAST
        }
    }
}
