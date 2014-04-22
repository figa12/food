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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import aau.sw8.model.User;


/**
 * Created by Sam on 14/04/2014.
 */
public abstract class BaseActivity extends Activity implements RecipeSearchFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected CharSequence drawerTitle;
    protected CharSequence title;
    private String[] pageTitles;
    protected static DrawerLayout drawerLayout;
    protected static LinearLayout drawerLinearLayout;
    protected static ListView drawerListView;
    protected static ActionBarDrawerToggle drawerToggle;
    protected static int FRAGMENT_CHANGE_REQUEST = 11; // can be any number
    protected static User user;
    private int actionBarHeight;
    private TypedValue typedValue = new TypedValue();
    private static final int RC_SIGN_IN = 0;
    protected static GoogleApiClient googleApiClient; // Google client to interact with Google API
    protected static boolean intentInProgress;
    protected static boolean signInClicked;
    protected static ConnectionResult connectionResult;
    protected static TextView drawerSignInBtn;

    // Logcat tag
    private static final String TAG = "BaseActivity";


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_layout); // "super" is used because "this" is overridden

        setupDrawer();

        googleApiClient = initGplusClient();

    }

    @SuppressWarnings("ConstantConditions")
    private void setupDrawer() {
        this.title = BaseActivity.this.drawerTitle = super.getTitle();
        this.pageTitles = super.getResources().getStringArray(R.array.pages_array);
        BaseActivity.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        BaseActivity.drawerLinearLayout = (LinearLayout) super.findViewById(R.id.left_drawer);
        BaseActivity.drawerListView = (ListView) super.findViewById(R.id.left_menu);


        BaseActivity.drawerSignInBtn = (TextView) super.findViewById(R.id.btn_sign_in_drawer);
        BaseActivity.drawerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((TextView)view).getText().equals("Sign in")){
                    //sign in
                    BaseActivity.this.selectItem(99);
                }else{
                    //sign out
                }
            }
        });


        // set a custom shadow that overlays the main content when the drawer opens
        BaseActivity.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        BaseActivity.drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, this.pageTitles));
        BaseActivity.drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseActivity.this.selectItem(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        super.getActionBar().setDisplayHomeAsUpEnabled(true);
        super.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        BaseActivity.drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                BaseActivity.drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                BaseActivity.super.getActionBar().setTitle(BaseActivity.this.title);
                BaseActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                BaseActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                BaseActivity.super.getActionBar().setTitle(BaseActivity.this.drawerTitle);
                BaseActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
                BaseActivity.super.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        BaseActivity.drawerLayout.setDrawerListener(BaseActivity.drawerToggle);
        super.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                BaseActivity.this.setActionBarArrowDependingOnFragmentsBackStack();
            }
        });
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

    protected void SignIn(){
        if(!googleApiClient.isConnecting()) {
            signInClicked = true;
            resolveSignInError();
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


    /**
     * Overridden to inflate the drawer layout instead of actually setting the content view
     * */
    @Override
    public void setContentView(final int layoutResID) {
        FrameLayout content = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, content, true);
        // here you can get your drawer buttons and define how they should behave and what must they do, so you won't be needing to repeat it in every activity class
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        super.getActionBar().setTitle(this.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open/close the drawer or act as back button
        if (BaseActivity.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (this.isDrawerOpen()) {
            BaseActivity.drawerLayout.closeDrawers();
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
        BaseActivity.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        BaseActivity.drawerToggle.onConfigurationChanged(newConfig);
    }

    /***
     * Return the state of the drawer.
     * @return isDrawerOpen
     */
    public boolean isDrawerOpen() {
        return BaseActivity.drawerLayout.isDrawerOpen(BaseActivity.drawerLinearLayout);
    }


    @SuppressWarnings("ConstantConditions")
    protected void selectItem(int position) {
        if (this.isTaskRoot()) { // Replace fragment if this is the main activity
            Fragment fragment;
            Bundle args = new Bundle();

            // Insert cases for other fragments here
            // the indexes corresponds to the order their page names are defined in strings.xml
            switch (position) {
                case 0:
                    fragment = new SearchFragment();
                    break;

                case 1:
                    fragment = new RecipeSearchFragment();
                    break;

                case 2:
                    fragment = new FavouriteFragment();
                    break;

                case 3:
                    fragment = new ShoppingListFragment();
                    break;

                default:
                    Toast.makeText(this, "Fragment not implemented", Toast.LENGTH_LONG).show();
                    return;
            }

            // Enable fragments to handle the action bar
            fragment.setHasOptionsMenu(false);

            // Clear the back stack
            while (super.getFragmentManager().popBackStackImmediate()) ;

            // give the fragment its position as argument
            args.putInt(MainActivity.ARG_POSITION, position);
            fragment.setArguments(args);

            // update selected item and title, then close the drawer
            BaseActivity.drawerListView.setItemChecked(position, true);
            this.setTitle(this.pageTitles[position]);
            BaseActivity.drawerLayout.closeDrawers();

            // replace the current view with the fragment
            super.getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        else {
            // close this activity and send a request to the parent activity to replace the fragment
            Intent intent = new Intent();
            intent.putExtra("position", position);
            this.setResult(RESULT_OK, intent);
            this.finish();
        }
    }



    /**
     * Called when an activity that has been started using "startActivityForResult" returns.
     * Allows recipe activities to request fragment change.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseActivity.FRAGMENT_CHANGE_REQUEST) {
            if (resultCode == RESULT_OK) { // child activity is requesting a fragment change
                // Get requested fragment id
                int position = data.getIntExtra("position", 0);

                // Change fragment
                this.selectItem(position);
            }
        }
        else if (requestCode == BaseActivity.RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                signInClicked = false;
            }

            intentInProgress = false;

            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }



    protected void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = super.getFragmentManager().getBackStackEntryCount();
        boolean shouldEnableDrawerIndicator = (this.isTaskRoot() && backStackEntryCount == 0);
        BaseActivity.drawerToggle.setDrawerIndicatorEnabled(shouldEnableDrawerIndicator);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO What does this do?!?!
        //mayhaps: A method that handles interactions from the current underlying Fragment
    }


    @SuppressWarnings("ConstantConditions")
    public int getActionBarHeight() {
        if (actionBarHeight != 0) {
            return actionBarHeight;
        }
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        return actionBarHeight;
    }
}