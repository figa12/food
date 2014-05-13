package aau.sw8.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import aau.sw8.model.Recipe;

public class MainActivity extends DrawerActivity implements IngredientSearchFragment.OnFragmentInteractionListener {

    /*Variables*/
    public static final String ARG_POSITION = "position";
    // Logcat tag
    private static final String TAG = "Mainactivity";
    public static String ingredientResult;
    private static IngredientSearchFragment ingredientSearchFragment;

    /*Override methods*/
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Image loader*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        if (savedInstanceState == null) {
            this.selectItem(0);
        } else {
            this.setActionBarArrowDependingOnFragmentsBackStack();
        }
    }

    @Override
    public void onFragmentInteraction(IngredientSearchFragment fragment) {
        ingredientSearchFragment = fragment;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;

    }

    /*Class methods*/

    @SuppressWarnings("ConstantConditions")
    public void openRecipeActivity(Recipe recipe) {
        super.dismissKeyboard();

        // Create an intent for a recipe activity
        Intent myIntent = new Intent(this, RecipeActivity.class);

        // Put the recipe into the intent
        myIntent.putExtra(RecipeActivity.ARG_RECIPE, recipe);

        // Start the activity and allow it to request fragment change
        this.startActivityForResult(myIntent, DrawerActivity.FRAGMENT_CHANGE_REQUEST);
    }

    @Override
    protected void onLoggedIn() {
        super.onLoggedIn();
    }

    @Override
    protected void onLoggedOut() {
        super.onLoggedOut();
    }
}