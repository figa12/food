package aau.sw8.recipe;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

import aau.sw8.data.ServerCom;
import aau.sw8.model.Recipe;
import aau.sw8.model.User;

public class MainActivity extends BaseActivity {

    /*Variables*/
    public static final String ARG_POSITION = "position";

    public ServerCom serverCom;

    // Logcat tag
    private static final String TAG = "Mainactivity";

    /*Override methods*/
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Image loader*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        /*Server communication*/
        serverCom = ServerCom.getInstance();
        serverCom.init(this);

        /* ServerCom testing //TODO should be removed at some point
        serverCom.startNewTask(new BasicNameValuePair("Request", "1"), new BasicNameValuePair("Data", "test1"));
        serverCom.startNewTask(new BasicNameValuePair("Request", "2"), new BasicNameValuePair("Data", "test2"));
        */

        /*Check the phone for sign-in tokens!*/
        //user = new User(1,1234,"KoenBjarne");

        //BaseActivity.user = null;

        if (savedInstanceState == null) {
            this.selectItem(0);
        } else {
            this.setActionBarArrowDependingOnFragmentsBackStack();
        }


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
            //   Intent wordIntent = new Intent(this, WordActivity.class);
            // wordIntent.setData(intent.getData());
            // startActivity(wordIntent);
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
        this.startActivityForResult(myIntent, BaseActivity.FRAGMENT_CHANGE_REQUEST);
    }
}