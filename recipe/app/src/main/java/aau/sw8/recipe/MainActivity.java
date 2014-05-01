package aau.sw8.recipe;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;

import aau.sw8.data.IngredientCom;
import aau.sw8.data.ServerComTask;
import aau.sw8.model.Ingredient;

import aau.sw8.model.Recipe;

public class MainActivity extends DrawerActivity {

    /*Variables*/
    public static final String ARG_POSITION = "position";
    // Logcat tag
    private static final String TAG = "Mainactivity";
    public static String ingredientResult;

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
        /* Also need to find out how to give different suggestions for ingredients and recipes */

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            // Intent wordIntent = new Intent(this, MainActivity.class);
             //wordIntent.setData(intent.getData());
           //  startActivity(wordIntent);

            Uri uri = intent.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            int wIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_WORD);

            ingredientResult = cursor.getString(wIndex);

           // SearchFragment searchFragment = (SearchFragment) super.getFragmentManager().findFragmentByTag("fisk");
           // searchFragment.updateFlowLayout();

            Toast.makeText(this, ingredientResult, Toast.LENGTH_SHORT).show();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //showResults(query);
        }
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