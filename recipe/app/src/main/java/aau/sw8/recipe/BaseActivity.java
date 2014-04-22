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
public abstract class BaseActivity extends Activity implements RecipeSearchFragment.OnFragmentInteractionListener, GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, PlusClient.OnAccessRevokedListener {

    protected CharSequence drawerTitle;
    protected CharSequence title;
    private String[] pageTitles;
    protected static DrawerLayout drawerLayout;
    protected static LinearLayout drawerLinearLayout;
    protected static ListView drawerListView;
    protected static ActionBarDrawerToggle drawerToggle;
    protected static int actionBarHeight = 0;
    protected static final int FRAGMENT_CHANGE_REQUEST = 11; // can be any number
    protected static User user;
    private int mActionBarHeight;
    private TypedValue mTypedValue = new TypedValue();
   /* private static final int RC_SIGN_IN = 0;
    protected static GoogleApiClient googleApiClient; // Google client to interact with Google API
    protected static boolean intentInProgress;
    protected static boolean signInClicked;*/
    protected static ConnectionResult connectionResult;
    protected static TextView drawerSignInBtn;
    // A progress dialog to display when the user is connecting in
    // case there is a delay in any of the dialogs being ready.
    protected ProgressDialog mConnectionProgressDialog;

    // A magic number we will use to know that our sign-in error
    // resolution activity has completed.
    private static final int OUR_REQUEST_CODE = 49404;

    // The core Google+ client.
    protected static PlusClient mPlusClient;

    // A flag to stop multiple dialogues appearing for the user.
    private boolean mResolveOnFail;

    // Logcat tag
    private static final String TAG = "BaseActivity";

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_layout); // "super" is used because "this" is overridden

        setupDrawer();

        //googleApiClient = initGplusClient();

        mPlusClient = new PlusClient.Builder(this, this, this).build();

        // We use mResolveOnFail as a flag to say whether we should trigger
        // the resolution of a connectionFailed ConnectionResult.
        mResolveOnFail = false;

        // Configure the ProgressDialog that will be shown if there is a
        // delay in presenting the user with the next sign in step.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
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

    /*
     * GooglePlus methods
     */

    public void SignIn(View view){
        switch (view.getId()) {
            case R.id.sign_in_button:
                Log.v(TAG, "Tapped sign in");
                if (!mPlusClient.isConnected()) {
                    // Show the dialog as we are now signing in.
                    mConnectionProgressDialog.show();
                    // Make sure that we will start the resolution (e.g. fire the
                    // intent and pop up a dialog for the user) for any errors
                    // that come in.
                    mResolveOnFail = true;
                    // We should always have a connection result ready to resolve,
                    // so we can start that process.
                    if (connectionResult != null) {
                        startResolution();
                    } else {
                        // If we don't have one though, we can start connect in
                        // order to retrieve one.
                        mPlusClient.connect();
                    }
                }
                break;
            case R.id.sign_out_button:
                Log.v(TAG, "Tapped sign out");
                // We only want to sign out if we're connected.
                if (mPlusClient.isConnected()) {
                    // Clear the default account in order to allow the user
                    // to potentially choose a different account from the
                    // account chooser.
                    mPlusClient.clearDefaultAccount();

                    // Disconnect from Google Play Services, then reconnect in
                    // order to restart the process from scratch.
                    mPlusClient.disconnect();
                    mPlusClient.connect();

                    // Hide the sign out buttons, show the sign in button.

                    /*findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
                    findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);*/
                }
                break;
            case R.id.revoke_access_button:
                Log.v(TAG, "Tapped disconnect");
                if (mPlusClient.isConnected()) {
                    // Clear the default account as in the Sign Out.
                    mPlusClient.clearDefaultAccount();

                    // Go away and revoke access to this entire application.
                    // This will call back to onAccessRevoked when it is
                    // complete as it needs to go away to the Google
                    // authentication servers to revoke all token.
                    mPlusClient.revokeAccessAndDisconnect(this);
                }
                break;
            default:
                // Unknown id.
        }
    }

    @Override
    public void onAccessRevoked(ConnectionResult status) {
        // mPlusClient is now disconnected and access has been revoked.
        // We should now delete any data we need to comply with the
        // developer properties. To reset ourselves to the original state,
        // we should now connect again. We don't have to disconnect as that
        // happens as part of the call.
        mPlusClient.connect();

        // Hide the sign out buttons, show the sign in button.
        /*findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);*/
    }

    @Override
    public void onDisconnected() {
        // Bye!
        Log.v(TAG, "Disconnected. Bye!");
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Yay! We can get the oAuth 2.0 access token we are using.
        Log.v(TAG, "Connected. Yay!");

        // Turn off the flag, so if the user signs out they'll have to
        // tap to sign in again.
        mResolveOnFail = false;

        // Hide the progress dialog if its showing.
        mConnectionProgressDialog.dismiss();

        // Retrieve the oAuth 2.0 access token.
        final Context context = this.getApplicationContext();
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String scope = "oauth2:" + Scopes.PLUS_LOGIN;
                String token = "";
                try {
                    // We can retrieve the token to check via
                    // tokeninfo or to pass to a service-side
                    // application.
                    token = GoogleAuthUtil.getToken(context,
                            mPlusClient.getAccountName(), scope);
                } catch (UserRecoverableAuthException e) {
                    // This error is recoverable, so we could fix this
                    // by displaying the intent to the user.
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.i(TAG, "Access token retrieved:" + token);
                setUser(mPlusClient.getAccountName(), token);
            }
        };
        task.execute((Void) null);
    }

    public void setUser(String accountName, String token){
        BaseActivity.user = new User(1, token, accountName);
    }

    protected void onStart() {
        super.onStart();
        Log.v(TAG, "Start");
        // Every time we start we want to try to connect. If it
        // succeeds we'll get an onConnected() callback. If it
        // fails we'll get onConnectionFailed(), with a result!
        mPlusClient.connect();
    }

    protected void onStop() {
        super.onStop();

        Log.v(TAG, "Stop");
        // It can be a little costly to keep the connection open
        // to Google Play Services, so each time our activity is
        // stopped we should disconnect.
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "ConnectionFailed");
        // Most of the time, the connection will fail with a
        // user resolvable result. We can store that in our
        // mConnectionResult property ready for to be used
        // when the user clicks the sign-in button.
        if (result.hasResolution()) {
            connectionResult = result;
            if (mResolveOnFail) {
                // This is a local helper function that starts
                // the resolution of the problem, which may be
                // showing the user an account chooser or similar.
                startResolution();
            }
        }
    }
    /**
     * A helper method to flip the mResolveOnFail flag and start the resolution
     * of the ConnenctionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a
            // result from the activity we're about to start.
            mResolveOnFail = false;
            // If we can resolve the error, then call start resolution
            // and pass it an integer tag we can use to track. This means
            // that when we get the onActivityResult callback we'll know
            // its from being started here.
            connectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new
            // ConnectionResult.
            mPlusClient.connect();
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

            case OUR_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    // If we have a successful result, we will want to be able to
                    // resolve any further errors, so turn on resolution with our
                    // flag.
                    mResolveOnFail = true;
                    // If we have a successful result, lets call connect() again. If
                    // there are any more errors to resolve we'll get our
                    // onConnectionFailed, but if not, we'll get onConnected.
                    mPlusClient.connect();
                }else {
                    // If we've got an error we can't resolve, we're no
                    // longer in the midst of signing in, so we can stop
                    // the progress spinner.
                    mConnectionProgressDialog.dismiss();
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
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }
}