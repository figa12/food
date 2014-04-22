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

public class MainActivity extends BaseActivity implements GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, PlusClient.OnAccessRevokedListener {

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

        BaseActivity.user = null;

        if (savedInstanceState == null) {
            this.selectItem(0);
        } else {
            this.setActionBarArrowDependingOnFragmentsBackStack();
        }

        /*Google plus sign in*/
        BaseActivity.mPlusClient = new PlusClient.Builder(this, this, this).build();

        // We use mResolveOnFail as a flag to say whether we should trigger
        // the resolution of a connectionFailed ConnectionResult.
        BaseActivity.mResolveOnFail = false;

        // Configure the ProgressDialog that will be shown if there is a
        // delay in presenting the user with the next sign in step.
        BaseActivity.mConnectionProgressDialog = new ProgressDialog(this);
        BaseActivity.mConnectionProgressDialog.setMessage("Signing in...");
    }

    /*
     * GooglePlus methods
     */
    public void SignIn(View view){
        switch (view.getId()) {
            case R.id.sign_in_button:
                Log.v(TAG, "Tapped sign in");
                if (!BaseActivity.mPlusClient.isConnected()) {
                    // Show the dialog as we are now signing in.
                    BaseActivity.mConnectionProgressDialog.show();
                    // Make sure that we will start the resolution (e.g. fire the
                    // intent and pop up a dialog for the user) for any errors
                    // that come in.
                    BaseActivity.mResolveOnFail = true;
                    // We should always have a connection result ready to resolve,
                    // so we can start that process.
                    if (BaseActivity.connectionResult != null) {
                        startResolution();
                    } else {
                        // If we don't have one though, we can start connect in
                        // order to retrieve one.
                        BaseActivity.mPlusClient.connect();
                    }
                }
                break;
            case R.id.sign_out_button:
                Log.v(TAG, "Tapped sign out");
                // We only want to sign out if we're connected.
                if (BaseActivity.mPlusClient.isConnected()) {
                    // Clear the default account in order to allow the user
                    // to potentially choose a different account from the
                    // account chooser.
                    BaseActivity.mPlusClient.clearDefaultAccount();

                    // Disconnect from Google Play Services, then reconnect in
                    // order to restart the process from scratch.
                    BaseActivity.mPlusClient.disconnect();
                    BaseActivity.mPlusClient.connect();

                    // Hide the sign out buttons, show the sign in button.

                    /*findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
                    findViewById(R.id.revoke_access_button).setVisibility(View.INVISIBLE);*/
                }
                break;
            case R.id.revoke_access_button:
                Log.v(TAG, "Tapped disconnect");
                if (BaseActivity.mPlusClient.isConnected()) {
                    // Clear the default account as in the Sign Out.
                    BaseActivity.mPlusClient.clearDefaultAccount();

                    // Go away and revoke access to this entire application.
                    // This will call back to onAccessRevoked when it is
                    // complete as it needs to go away to the Google
                    // authentication servers to revoke all token.
                    BaseActivity.mPlusClient.revokeAccessAndDisconnect(this);
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
        BaseActivity.mPlusClient.connect();

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
        BaseActivity.mResolveOnFail = false;

        // Hide the progress dialog if its showing.
        BaseActivity.mConnectionProgressDialog.dismiss();

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
                            BaseActivity.mPlusClient.getAccountName(), scope);
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
                setUser(BaseActivity.mPlusClient.getAccountName(), token);
            }
        };
        task.execute((Void) null);
    }

    public void setUser(String accountName, String token){
        BaseActivity.user = new User(1, token, accountName);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "ConnectionFailed");
        // Most of the time, the connection will fail with a
        // user resolvable result. We can store that in our
        // mConnectionResult property ready for to be used
        // when the user clicks the sign-in button.
        if (result.hasResolution()) {
            BaseActivity.connectionResult = result;
            if (BaseActivity.mResolveOnFail) {
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
            BaseActivity.mResolveOnFail = false;
            // If we can resolve the error, then call start resolution
            // and pass it an integer tag we can use to track. This means
            // that when we get the onActivityResult callback we'll know
            // its from being started here.
            BaseActivity.connectionResult.startResolutionForResult(this, BaseActivity.OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new
            // ConnectionResult.
            BaseActivity.mPlusClient.connect();
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

            case BaseActivity.OUR_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    // If we have a successful result, we will want to be able to
                    // resolve any further errors, so turn on resolution with our
                    // flag.
                    BaseActivity.mResolveOnFail = true;
                    // If we have a successful result, lets call connect() again. If
                    // there are any more errors to resolve we'll get our
                    // onConnectionFailed, but if not, we'll get onConnected.
                    BaseActivity.mPlusClient.connect();
                }else {
                    // If we've got an error we can't resolve, we're no
                    // longer in the midst of signing in, so we can stop
                    // the progress spinner.
                    BaseActivity.mConnectionProgressDialog.dismiss();
                }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        super.getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchBar = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBar.setIconifiedByDefault(false);
        searchBar.setQueryHint("Enter a recipe name");

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