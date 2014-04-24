package aau.sw8.recipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;

import java.io.IOException;

import aau.sw8.model.User;

/**
 * Created by jacob on 4/24/14.
 */
public abstract class LogInActivity extends Activity implements GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, PlusClient.OnAccessRevokedListener {
    protected static User user;                                  //User of the application

    protected ProgressDialog mConnectionProgressDialog;          //Process dialog for sign in.
    protected ConnectionResult connectionResult;
    protected static final int OUR_REQUEST_CODE = 49404;         //A magic number we will use to know that our sign-in error, resolution activity has completed.
    protected PlusClient plusClient;                             //The core Google+ client.
    protected boolean resolveOnFail;                             //A flag to stop multiple dialogues appearing for the user.

    public static final int SIGN_IN = 1;
    public static final int SIGN_OUT = 2;
    public static final int REWOKE_ACCESS = 3;

    private static final String TAG = "LogInActivity";

    //TODO: should be remove or changed to false when other sign in methods are implemented.
    protected final boolean isOnlyGooglePlus = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupGooglePlus();
    }

    /***
     * This method is called when the activity is starts
     * the method tries to connect to google plus.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "Activity Started");
        if(this.plusClient != null) {
            if(!this.plusClient.isConnected()) {
                Log.v(TAG, "Connection Started");
                // Every time we start we want to try to connect. If it
                // succeeds we'll get an onConnected() callback. If it
                // fails we'll get onConnectionFailed(), with a result!
                this.plusClient.connect();
            }
        }
    }

    /***
     * This method is called when the activity is stops
     * the method stops the connection between the application and google sign in.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "Activity stopped");
        if(this.plusClient != null) {
            if(this.plusClient.isConnected()) {
                Log.v(TAG, "Connection Closed");
                // It can be a little costly to keep the connection open
                // to Google Play Services, so each time our activity is
                // stopped we should disconnect.
                this.plusClient.disconnect();
            }
        }
    }

    /***
     * When the log in is successful, and an connection as been established this method is called.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Yay! We can get the oAuth 2.0 access token we are using.
        Log.v(TAG, "Connected. Yay!");

        // Turn off the flag, so if the user signs out they'll have to
        // tap to sign in again.
        this.resolveOnFail = false;

        // Hide the progress dialog if its showing.
        this.mConnectionProgressDialog.dismiss();

        //Update UI
        this.updateUserUI(true);

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
                            LogInActivity.this.plusClient.getAccountName(), scope);
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
                LogInActivity.this.setUser(LogInActivity.this.plusClient.getAccountName(), token);
            }
        };
        task.execute((Void) null);
    }

    @Override
    public void onDisconnected() {
        // Bye!
        Log.v(TAG, "Disconnected. Bye!");
    }

    /***
     * On revoke access called
     * @param status
     */
    @Override
    public void onAccessRevoked(ConnectionResult status) {
        // plusClient is now disconnected and access has been revoked.
        // We should now delete any data we need to comply with the
        // developer properties. To reset ourselves to the original state,
        // we should now connect again. We don't have to disconnect as that
        // happens as part of the call.
        this.plusClient.connect();

        // Hide the sign out buttons, show the sign in button.
        updateUserUI(false);
    }

    /***
     * If the sign in to Google plus failed this method is called.
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "ConnectionFailed");
        // Most of the time, the connection will fail with a
        // user resolvable result. We can store that in our
        // mConnectionResult property ready for to be used
        // when the user clicks the sign-in button.
        if (result.hasResolution()) {
            this.connectionResult = result;
            if (this.resolveOnFail) {
                // This is a local helper function that starts
                // the resolution of the problem, which may be
                // showing the user an account chooser or similar.
                startResolution();
            }
        }
    }

    /**
     * Called when an activity that has been started using "startActivityForResult" returns.
     * Allows recipe activities to request fragment change.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "ActivityResult: " + requestCode);

        if(requestCode == DrawerActivity.OUR_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                // If we have a successful result, we will want to be able to
                // resolve any further errors, so turn on resolution with our
                // flag.
                this.resolveOnFail = true;
                // If we have a successful result, lets call connect() again. If
                // there are any more errors to resolve we'll get our
                // onConnectionFailed, but if not, we'll get onConnected.
                this.plusClient.connect();
            }else {
                // If we've got an error we can't resolve, we're no
                // longer in the midst of signing in, so we can stop
                // the progress spinner.
                this.mConnectionProgressDialog.dismiss();
            }
        }
    }

    /***
     * This methods sets up the plusClient,
     * so the application is able to sign in to google plus.
     */
    private void setupGooglePlus(){
        /*Google plus sign in*/
        this.plusClient = new PlusClient.Builder(this, this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();

        // We use resolveOnFail as a flag to say whether we should trigger
        // the resolution of a connectionFailed ConnectionResult.
        this.resolveOnFail = false;

        // Configure the ProgressDialog that will be shown if there is a
        // delay in presenting the user with the next sign in step.
        this.mConnectionProgressDialog = new ProgressDialog(this);
        this.mConnectionProgressDialog.setMessage("Signing in...");
    }

    /***
     * The google plus log in actions, use the following actions: DrawerActivity.SIGN_IN, .SIGN_OUT, .REVOKE_ACCESS
     * @param action
     */
    public void googlePlusLogInActions(int action){
        switch (action) {
            case DrawerActivity.SIGN_IN:
                Log.v(TAG, "Tapped sign in");
                if (!this.plusClient.isConnected()) {
                    // Show the dialog as we are now signing in.
                    this.mConnectionProgressDialog.show();
                    // Make sure that we will start the resolution (e.g. fire the
                    // intent and pop up a dialog for the user) for any errors
                    // that come in.
                    this.resolveOnFail = true;
                    // We should always have a connection result ready to resolve,
                    // so we can start that process.
                    if (this.connectionResult != null) {
                        startResolution();
                    } else {
                        // If we don't have one though, we can start connect in
                        // order to retrieve one.
                        this.plusClient.connect();
                    }
                }
                break;
            case DrawerActivity.SIGN_OUT:
                Log.v(TAG, "Tapped sign out");
                // We only want to sign out if we're connected.
                if (this.plusClient.isConnected()) {
                    // Clear the default account in order to allow the user
                    // to potentially choose a different account from the
                    // account chooser.
                    this.plusClient.clearDefaultAccount();

                    // Disconnect from Google Play Services, then reconnect in
                    // order to restart the process from scratch.
                    this.plusClient.disconnect();
                    this.plusClient.connect();

                    // Sets the user to null, meaning not signed in
                    DrawerActivity.user = null;

                    // Hide the sign out buttons, show the sign in button.
                    updateUserUI(false);
                }
                break;
            case DrawerActivity.REWOKE_ACCESS:
                Log.v(TAG, "Tapped disconnect");
                if (this.plusClient.isConnected()) {
                    // Clear the default account as in the Sign Out.
                    this.plusClient.clearDefaultAccount();

                    // Go away and revoke access to this entire application.
                    // This will call back to onAccessRevoked when it is
                    // complete as it needs to go away to the Google
                    // authentication servers to revoke all token.
                    this.plusClient.revokeAccessAndDisconnect(this);

                    // Sets the user to null, meaning not signed in
                    DrawerActivity.user = null;

                    /*Update of UI is done later in "OnAccessRevoked"*/
                }
                break;
            default:
                // Unknown id.
        }
    }

    /***
     * Sets the user of the application.
     * @param accountName
     * @param token
     */
    public void setUser(String accountName, String token){
        /*Query the database for the user id
        * if he does not exist insert the user.*/
        int userid = 1;

        LogInActivity.user = new User(userid, token, accountName);
    }

    /**
     * A helper method to flip the resolveOnFail flag and start the resolution
     * of the ConnenctionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a
            // result from the activity we're about to start.
            this.resolveOnFail = false;
            // If we can resolve the error, then call start resolution
            // and pass it an integer tag we can use to track. This means
            // that when we get the onActivityResult callback we'll know
            // its from being started here.
            this.connectionResult.startResolutionForResult(this, DrawerActivity.OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new
            // ConnectionResult.
            this.plusClient.connect();
        }
    }

    protected abstract void updateUserUI(boolean isLoggedIn);
}
