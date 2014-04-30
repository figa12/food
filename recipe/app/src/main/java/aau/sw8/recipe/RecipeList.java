package aau.sw8.recipe;

import android.app.ProgressDialog;
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

import aau.sw8.data.RecipeCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.IntermediateRecipe;
import aau.sw8.model.Recipe;

/**
 * Created by Jesper on 07-03-14.
 */
public class RecipeList extends ListLinearLayout<IntermediateRecipe> {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();

    private View focusedView = null;

    private ProgressDialog progressDialog;

    public RecipeList(Context context) {
        super(context);
        this.construct(context);
    }

    public RecipeList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.construct(context);
    }

    @SuppressWarnings("ConstantConditions")
    private void construct(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("Loading");
        this.progressDialog.setCancelable(false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected View makeView(IntermediateRecipe recipe) {
        // Get inflater and inflate item
        LayoutInflater layoutInflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View recipeView = layoutInflater.inflate(R.layout.recipe_list_item, null);

        // Set recipe image
        ImageView recipeImage = (ImageView) recipeView.findViewById(R.id.recipeImageView);
        this.imageLoader.displayImage(recipe.getImage(), recipeImage, this.imageLoaderOptions);

        // Set recipe title
        TextView recipeTitle = (TextView) recipeView.findViewById(R.id.titleTextView);
        recipeTitle.setText(recipe.getName());

        // Find the clickable part of the item layout and add the recipe as a tag
        View clickableLayout = recipeView.findViewById(R.id.mainLayout);
        clickableLayout.setTag(recipe);

        // .. and add the touch listener to it
        clickableLayout.setOnTouchListener(new RecipeOnTouchListener(recipe));

        return recipeView;
    }

    @SuppressWarnings("ConstantConditions")
    protected void onClick(View view) {
        // Flash the image
        flashView(view);

        // The tag is the recipe which was clicked
        IntermediateRecipe recipe = (IntermediateRecipe) view.getTag();

        // downloads and open RecipeActivity
        this.openRecipe(recipe.getId());
    }

    @SuppressWarnings("ConstantConditions")
    private void openRecipe(long id) {
        this.progressDialog.show();

        new RecipeCom((DrawerActivity) super.getContext(), new ServerComTask.OnResponseListener<Recipe>() {
            @Override
            public void onResponse(Recipe result) {
                RecipeList.this.progressDialog.dismiss();

                if (result != null) {
                    // Open recipe fragment
                    ((MainActivity) RecipeList.super.getContext()).openRecipeActivity(result);
                }
            }

            @Override
            public void onFailed() {
                RecipeList.this.progressDialog.dismiss();
            }
        }, id);
    }

    protected void onLongClick(IntermediateRecipe recipe, View view) { }

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

    private class RecipeOnTouchListener implements OnTouchListener{
        private IntermediateRecipe recipe;
        public RecipeOnTouchListener(IntermediateRecipe recipe) {
            this.recipe = recipe;
        }

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
                            onLongClick(recipe, focusedView);
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
    }
}
