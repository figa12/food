package aau.sw8.recipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;

import java.io.IOException;

import aau.sw8.model.User;


/**
 * Created by Sam on 14/04/2014.
 */
public abstract class BaseActivity extends Activity implements RecipeSearchFragment.OnFragmentInteractionListener {

    protected CharSequence drawerTitle;
    protected CharSequence title;
    private String[] pageTitles;
    protected static DrawerLayout drawerLayout;
    protected static LinearLayout drawerLinearLayout;
    protected static ListView drawerListView;
    protected static ActionBarDrawerToggle drawerToggle;
    protected static final int FRAGMENT_CHANGE_REQUEST = 11; // can be any number
    protected static User user;
    private int actionBarHeight;
    private TypedValue typedValue = new TypedValue();
    /*private static final int RC_SIGN_IN = 0;
    protected static GoogleApiClient googleApiClient; // Google client to interact with Google API
    protected static boolean intentInProgress;
    protected static boolean signInClicked;*/
    protected static ConnectionResult connectionResult;
    protected static TextView drawerSignInBtn;

    protected static ProgressDialog mConnectionProgressDialog;          //Process dialog for sign in.
    protected static final int OUR_REQUEST_CODE = 49404;                //A magic number we will use to know that our sign-in error, resolution activity has completed.
    protected static PlusClient mPlusClient;                            //The core Google+ client.
    protected static boolean mResolveOnFail;                            //A flag to stop multiple dialogues appearing for the user.

    private static final String TAG = "BaseActivity";



    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_layout); // "super" is used because "this" is overridden

        if(user != null) {
            Log.w(TAG, BaseActivity.user.getUsername());
        }
        setupDrawer();
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
                if (((TextView) view).getText().equals("Sign in")) {
                    //sign in
                    BaseActivity.this.selectItem(99);
                } else {
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
    @Override
    protected void onStart() {
        super.onStart();

        if(BaseActivity.mPlusClient != null) {
            Log.v(TAG, "Start");
            // Every time we start we want to try to connect. If it
            // succeeds we'll get an onConnected() callback. If it
            // fails we'll get onConnectionFailed(), with a result!
            BaseActivity.mPlusClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(BaseActivity.mPlusClient != null) {
            Log.v(TAG, "Stop");
            // It can be a little costly to keep the connection open
            // to Google Play Services, so each time our activity is
            // stopped we should disconnect.
            BaseActivity.mPlusClient.disconnect();
        }
    }


    /**
     * Called when an activity that has been started using "startActivityForResult" returns.
     * Allows recipe activities to request fragment change.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "ActivityResult: " + requestCode);

        switch (requestCode){
            case BaseActivity.FRAGMENT_CHANGE_REQUEST:
                if (resultCode == RESULT_OK) { // child activity is requesting a fragment change
                    // Get requested fragment id
                    int position = data.getIntExtra("position", 0);

                    // Change fragment
                    this.selectItem(position);
                }
                break;
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

                case 99:
                    fragment = new SignInFragment();
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
            if(position != 99){
                this.setTitle(this.pageTitles[position]);
            }else{
                this.setTitle("Sign In");
            }
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