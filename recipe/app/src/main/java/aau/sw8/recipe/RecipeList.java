package aau.sw8.recipe;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aau.sw8.model.Recipe;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeList extends ListLinearLayout<Recipe> {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private View focusedView = null;

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

        // Find the clickable part of the item layout and add the recipe as a tag
        View clickableLayout = recipeView.findViewById(R.id.mainLayout);
        clickableLayout.setTag(recipe);

        // .. and add the touch listener to it
        clickableLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Get the image of the view being touched
                ImageView image = (ImageView)view.findViewById(R.id.recipeImageView);

                // Check whether the action is down and there are no focused view
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && focusedView == null) {
                    // Set this image as the current focused
                    focusedView = view;

                    // Schedule a task to highlight the image if it hasn't been canceled before it is run
                    focusedView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (focusedView != null) {
                                highlightView(focusedView);
                            }
                        }
                    }, ViewConfiguration.getTapTimeout());

                    focusedView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (focusedView != null)
                                onLongClick(focusedView);
                        }
                    }, ViewConfiguration.getLongPressTimeout());
                }
                // Check whether the action involves the current focused view
                else if (focusedView == view) {
                    // If the action is UP, it is a click
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        onClick(focusedView);
                        focusedView = null;
                    }
                    // If the action is CANCEL, the focus and highlight should be removed
                    else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        clearHighlight(focusedView);
                        focusedView = null;
                    }
                }

                return false;
            }
        });

        return recipeView;
    }

    protected void onClick(View view) { }

    protected void onLongClick(View view) { }

    protected void clearHighlight(View view){
        ImageView image = (ImageView)view.findViewById(R.id.recipeImageView);
        image.setColorFilter(null);
    }

    protected void highlightView(View view) {
        ImageView image = (ImageView)view.findViewById(R.id.recipeImageView);
        image.setColorFilter(0xFF222222, PorterDuff.Mode.ADD);
    }

    protected void flashView(View view) {
        ImageView image = (ImageView)view.findViewById(R.id.recipeImageView);
        image.setColorFilter(0xFF666666, PorterDuff.Mode.ADD);
    }
}
