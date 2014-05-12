package aau.sw8.recipe;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //TODO Implement what happens when the user clicks a suggestion

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion;

            ingredientSearchFragment.searchBar.setText("");
            ingredientSearchFragment.searchBar.clearFocus();

            Uri uri = intent.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            int wIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_WORD);

            ingredientResult = cursor.getString(wIndex);

            if (ingredientSearchFragment != null) {
                ingredientSearchFragment.updateFlowLayout();
            }
        }
        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {

            ingredientSearchFragment.searchBar.setText("");
            ingredientSearchFragment.searchBar.clearFocus();

            String query = intent.getStringExtra(SearchManager.QUERY);
            if (ingredientSearchFragment != null) {
                ingredientSearchFragment.addIngredientToFlowLayout(query);
            }

        }
    }


    @Override
    public void onBackPressed() {

        if (ingredientSearchFragment != null && ingredientSearchFragment.popupLayout.getVisibility() == View.VISIBLE) {
            ingredientSearchFragment.popupLayout.setVisibility(View.GONE);
            ingredientSearchFragment.searchBar.setText("");
        } else {
            super.onBackPressed();
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
}