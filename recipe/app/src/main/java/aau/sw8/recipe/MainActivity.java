package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import aau.sw8.data.ServerCom;
import aau.sw8.model.Recipe;
import aau.sw8.model.User;

public class MainActivity extends Activity implements SearchFragment.OnFragmentInteractionListener,
        ShoppingListFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    /*Variables*/
    public static final String ARG_POSITION = "position";

    private DrawerLayout drawerLayout;
    private ListView drawerPagesListView;
    private ListView drawerSignInListView;
    private LinearLayout drawerLinearLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView drawerSignInBtn;

    private User user;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] pageTitles;
    private String[] signInTiles;

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
        setContentView(R.layout.activity_main);

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
        //user = null;

        // Initializing google plus api client
        googleApiClient = initGplusClient();


        this.title = this.drawerTitle = super.getTitle();
        this.pageTitles = super.getResources().getStringArray(R.array.pages_array);
        this.signInTiles = super.getResources().getStringArray(R.array.sign_in_array);
        this.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        this.drawerLinearLayout = (LinearLayout) super.findViewById(R.id.left_drawer);
        this.drawerPagesListView = (ListView) super.findViewById(R.id.left_menu);


        this.drawerSignInBtn = (TextView) super.findViewById(R.id.btn_sign_in_drawer);
        this.drawerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((TextView)view).getText().equals("Sign in")){
                    //Sign in
                    MainActivity.this.signInGplus(view);
                }else{
                    //sign out
                    MainActivity.this.signOutGplus(view);
                }
            }
        });

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        this.drawerPagesListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, this.pageTitles));
        this.drawerPagesListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.selectItem(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        super.getActionBar().setDisplayHomeAsUpEnabled(true);
        super.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        this.drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                this.drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                MainActivity.super.getActionBar().setTitle(MainActivity.this.title);
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                MainActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                MainActivity.super.getActionBar().setTitle(MainActivity.this.drawerTitle);
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                MainActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        this.drawerLayout.setDrawerListener(this.drawerToggle);
        super.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
            }
        });

        if (savedInstanceState == null) {
            this.selectItem(0);
        } else {
            MainActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
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

    @Override
    public void onConnected(Bundle bundle) {
        signInClicked = false;

        // Get user's information
        //getProfileInformation();
        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            String personName = currentPerson.getDisplayName();
            String personGooglePlusProfile = currentPerson.getUrl();
            Toast.makeText(this, personName,Toast.LENGTH_SHORT).show();
        }

        // Update the UI after signin
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
        updateUI(false);
    }

    /***
     * Override method for handling connection errors during sign in with Gplus
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!intentInProgress) {
            // Store the ConnectionResult for later usage
            connectionResult = result;

            if (signInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    /***
     * Callback method for Gplus signin
     * @param requestCode
     * @param responseCode
     * @param intent
     */
    @Override
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        super.getActionBar().setTitle(this.title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = this.isDrawerOpen();

        /*Hide all the menu items, and show when entering the correct fragment*/
        menu.findItem(R.id.favourite_button).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    /***
     * Method for handling menu item touches.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (this.isDrawerOpen()) {
            this.drawerLayout.closeDrawers();
            return true;
        } else if (item.getItemId() == android.R.id.home && super.getFragmentManager().popBackStackImmediate()) {
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO What does this do?!?!
        //mayhaps: A method that handles interactions from the current underlying Fragment
    }

    /*Class methods*/
    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Toast.makeText(this, "The user is signed in", Toast.LENGTH_SHORT).show();

            drawerSignInBtn.setText(R.string.sign_out);
            /*btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);*/
        } else {
            Toast.makeText(this, "The user is signed out", Toast.LENGTH_SHORT).show();

            drawerSignInBtn.setText(R.string.sign_in);
            /*btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);*/
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                intentInProgress = false;
                googleApiClient.connect();
            }
        }
    }

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
     * Method for determining what fragment to open, it depends on the selection in
     * the navigation drawer.
     * @param position
     */
    @SuppressWarnings("ConstantConditions")
    private void selectItem(int position) {

        Fragment fragment = null; // update the main content by replacing fragments
        Bundle args = new Bundle();

        // Insert cases for other fragments here
        // the indexes corresponds to the order their page names are defined in strings.xml
        switch (position)
        {
            case 0:
                fragment = new SearchFragment();
                break;

            case 1:
                fragment = new FavouriteFragment();
                break;

            case 2:
                fragment = new ShoppingListFragment();
                break;
            default:
                Toast.makeText(this, "Fragment not implemented", Toast.LENGTH_LONG).show();
                return;
        }

        // Enable fragments to handle the action bar
        fragment.setHasOptionsMenu(true);

        while (super.getFragmentManager().popBackStackImmediate());

        // give the fragment its position as argument
        args.putInt(MainActivity.ARG_POSITION, position);
        fragment.setArguments(args);

        // replace the current view with the fragment
        super.getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        this.drawerPagesListView.setItemChecked(position, true);
        this.setTitle(this.pageTitles[position]);
        this.drawerLayout.closeDrawer(this.drawerLinearLayout);
    }

    /***
     * Gets the currently signed in user
     * @return
     */
    public User getUser() {
        return user;
    }

    private void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = super.getFragmentManager().getBackStackEntryCount();
        boolean shouldEnableDrawerIndicator = backStackEntryCount == 0;
        this.drawerToggle.setDrawerIndicatorEnabled(shouldEnableDrawerIndicator);
    }

    /***
     * Return the state of the drawer.
     * @return isDrawerOpen
     */
    public boolean isDrawerOpen() {
        return this.drawerLayout.isDrawerOpen(this.drawerLinearLayout);
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
    public void openRecipeFragment(Recipe recipe) {
        Fragment recipeFragment = new RecipeFragment();
        recipeFragment.setHasOptionsMenu(true);

        Bundle args = new Bundle();
        args.putParcelable(RecipeFragment.ARG_RECIPE, recipe);
        recipeFragment.setArguments(args);

        // replace the current view with the fragment
        super.getFragmentManager().beginTransaction().replace(R.id.content_frame, recipeFragment).addToBackStack(this.title.toString()).commit();

        // update selected item and title, then close the drawer
        this.drawerPagesListView.setItemChecked(this.drawerPagesListView.getSelectedItemPosition(), false);
    }
}
