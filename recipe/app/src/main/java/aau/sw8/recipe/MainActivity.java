package aau.sw8.recipe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import aau.sw8.data.ServerCom;
import aau.sw8.model.Recipe;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    /*Variables*/
    public static final String ARG_POSITION = "position";

    private static Recipe mRecipe;

    public ServerCom serverCom;

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient googleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean intentInProgress;

    private boolean signInClicked;

    private ConnectionResult connectionResult;


    /*Override methods*/
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set height of actionbar
        if (actionBarHeight == 0) {
            TypedValue mTypedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
            actionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        }

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
        BaseActivity.user = null;

        /*
        googleApiClient = initGplusClient();
        
        this.signInTiles = super.getResources().getStringArray(R.array.sign_in_array);
        this.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        this.drawerLinearLayout = (LinearLayout) super.findViewById(R.id.left_drawer);
        this.drawerPagesListView = (ListView) super.findViewById(R.id.left_menu);


        this.drawerSignInBtn = (TextView) super.findViewById(R.id.btn_sign_in_drawer);
        this.drawerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((TextView)view).getText().equals("Sign in")){
                    //sign in
                    MainActivity.this.selectItem(99);
                }else{
                    //sign out
                }
            }
        });
         */
        
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

    /***
     * Initialize the GooglePlusClient
     */
    private GoogleApiClient initGplusClient(){
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    /***
     * Override method for handling connection errors during sign in with Gplus
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!intentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            connectionResult = result;

            if (signInClicked) {
                // The user has already clicked 'sign-in' so we attempt to                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        signInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
    }

    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                signInClicked = false;
            }

            intentInProgress = false;

            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                intentInProgress = false;
                googleApiClient.connect();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchBar = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBar.setIconifiedByDefault(false);
        searchBar.setQueryHint("Enter a recipe name");

        return true;
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = this.isDrawerOpen();

        /*Hide all the menu items, and show when entering the correct fragment*/
        menu.findItem(R.id.action_search).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }


    /*Class methods*/
    /***
     * Method to sign in to Gplus
     * @param view
     */
    public void signInGplus(View view){
        if (!googleApiClient.isConnecting()) {
            googleApiClient.connect();
            signInClicked = true;
            ((TextView) view).setText(R.string.sign_out);
        }
    }

    /***
     * Method to sign out of GLus
     * @param view
     */
    public void signOutGplus(View view){
        if(googleApiClient.isConnected()){
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();

        }
    }

    /***
     * Method to revoke acces to Gplus
     * @param view
     */
    public void revokeGplusAccess(View view){

    }

    /***
     * Return the state of the drawer.
     * @return isDrawerOpen
     */
    /*
    public boolean isDrawerOpen() {
        return this.drawerLayout.isDrawerOpen(this.drawerLinearLayout);
    }
    */
    
    public void SignIn(){
        if(!googleApiClient.isConnecting()) {
            signInClicked = true;
            resolveSignInError();
        }
    }

    /***
     * Test method(callback method from a finished task)
     * @param result
     */
    public void onServerTest(String result){
        //TODO: Either delete this method of use it for something good.
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }



    @SuppressWarnings("ConstantConditions")
    public void openRecipeActivity(Recipe recipe) {
        // Create an intent for a recipe activity
        Intent myIntent = new Intent(this, RecipeActivity.class);

        // Put the recipe into the intent
        myIntent.putExtra(RecipeActivity.ARG_RECIPE, recipe);

        // Start the activity and allow it to request fragment change
        this.startActivityForResult(myIntent, BaseActivity.FRAGMENT_CHANGE_REQUEST);
    }
}