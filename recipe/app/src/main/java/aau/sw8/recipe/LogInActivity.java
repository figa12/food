package aau.sw8.recipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


import aau.sw8.data.ServerComTask;
import aau.sw8.model.ConnectivityReceiver;
import aau.sw8.model.User;

/**
 * Created by jacob on 4/24/14.
 */
public abstract class LogInActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ServerComTask.ServerAlertDialog {
    protected static User user;                                  //User of the application

    protected ProgressDialog connectionProgressDialog;          //Process dialog for sign in.
    protected ConnectionResult connectionResult;
    private static final int RC_SIGN_IN = 0;
    protected ConnectivityReceiver connectivityReceiver;

    protected static GoogleApiClient googleApiClient;                  //The core Google+ client.

    private boolean intentInProgress;
    private boolean signInClicked;

    protected AlertDialog serverAlertDialog;

    public static final int SIGN_IN = 1;
    public static final int SIGN_OUT = 2;
    public static final int REWOKE_ACCESS = 3;
    public static boolean IS_NETWORK_AVAILABLE;

    private static final String TAG = "LogInActivity";

    //TODO: should be remove or changed to false when other sign in methods are implemented.
    public static final boolean IS_ONLY_GOOGLE_PLUS = true; //if google plus is the only sign in method


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.serverAlertDialog = this.createAlertDialog();

        this.setupGooglePlus();
    }

    /***
     * This method is called when the activity is starts
     * the method tries to connect to google plus.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "Activity Started");
        if(googleApiClient != null) {
            if(!googleApiClient.isConnected()) {
                Log.v(TAG, "Connection Started");
                // Every time we start we want to try to connect. If it
                // succeeds we'll get an onConnected() callback. If it
                // fails we'll get onConnectionFailed(), with a result!
                this.updateUserUI(false);
                googleApiClient.connect();
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
        if(googleApiClient != null) {
            if(googleApiClient.isConnected()) {
                Log.v(TAG, "Connection Closed");
                // It can be a little costly to keep the connection open
                // to Google Play Services, so each time our activity is
                // stopped we should disconnect.
                googleApiClient.disconnect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        this.signInClicked = false;

        // Hide the progress dialog if its showing.
        this.connectionProgressDialog.dismiss();

        //Update UI
        this.updateUserUI(true);

        // Get user's information
        getProfileInformation();
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

    @Override
    public void onConnectionSuspended(int arg0) {
        googleApiClient.connect();
        //this.updateUserUI(false);
    }


    /***
     * If the sign in to Google plus failed this method is called.
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "ConnectionFailed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,0).show();
            return;
        }

        if (!intentInProgress) {
            // Store the ConnectionResult for later usage
            connectionResult = result;

            if (signInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                this.resolveSignInError();
            }
        }
    }

    /**
     * Called when an activity that has been started using "startActivityForResult" returns.
     * Allows recipe activities to request fragment change.
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        Log.v(TAG, "ActivityResult: " + requestCode);

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

    /***
     * This methods sets up the googleApiClient,
     * so the application is able to sign in to google plus.
     */
    private void setupGooglePlus(){
        /*Google plus sign in*/
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        // Configure the ProgressDialog that will be shown if there is a
        // delay in presenting the user with the next sign in step.
        this.connectionProgressDialog = new ProgressDialog(this);
        this.connectionProgressDialog.setMessage("Signing in...");
    }



    /***
     * The google plus log in actions, use the following actions: LogInActivity.SIGN_IN, .SIGN_OUT, .REVOKE_ACCESS
     * @param action
     */
    public void googlePlusLogInActions(int action){
        switch (action) {
            case LogInActivity.SIGN_IN:
                Log.v(TAG, "Tapped sign in");
                if (!googleApiClient.isConnecting()) {
                    this.connectionProgressDialog.show();
                    this.signInClicked = true;
                    this.resolveSignInError();
                }
                break;

            case LogInActivity.SIGN_OUT:
                Log.v(TAG, "Tapped sign out");
                // We only want to sign out if we're connected.
                if (googleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                    this.updateUserUI(false);
                    LogInActivity.user = null; // Sets the user to null, meaning not signed in
                    this.onLoggedOut();
                }
                break;

            case LogInActivity.REWOKE_ACCESS:
                Log.v(TAG, "Tapped disconnect");
                if (googleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient)
                            .setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status arg0) {
                                    Log.e(TAG, "User access revoked!");
                                    googleApiClient.connect();
                                    updateUserUI(false);
                                    LogInActivity.user = null; // Sets the user to null, meaning not signed in
                                }
                            });
                }
                break;
            default:
                // Unknown id.
        }
    }

    public void setUser(String personName, String email){
        /*Set the user, the user will be created in the database
        * when adding a recipe to favourites or any action that requires a user.
        * */
        String hash = User.doHash(email);
        LogInActivity.user = new User(personName, hash);
        Log.w(LogInActivity.TAG, "Current user is {personName: " + LogInActivity.user.getPersonName() + " hash: " + LogInActivity.user.getHash() +  "}");
        this.onLoggedIn(); //reports to the app that a user as signed in.
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        if(LogInActivity.IS_NETWORK_AVAILABLE){
            try {
                if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                    String personName = currentPerson.getDisplayName();
                    String email = Plus.AccountApi.getAccountName(googleApiClient);
                    LogInActivity.this.setUser(personName, email);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            String email = Plus.AccountApi.getAccountName(googleApiClient);
            ConnectivityReceiver.NEED_USER_INFO = true;
            LogInActivity.this.setUser("", email);
        }
    }

    //TODO: makes this work
    public static void setUserPersonName(){
        try {
            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                String personName = currentPerson.getDisplayName();
                LogInActivity.user.setPersonName(personName);
                ConnectivityReceiver.NEED_USER_INFO = false;
                Log.d(TAG, "personname: " + LogInActivity.user.getPersonName());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private AlertDialog createAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //myAlertDialog.setTitle("Title");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Server connection failed.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //empty
            }
        });
        return alertDialogBuilder.create();
    }

    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else {
            return true;
        }
    }


    public AlertDialog getServerAlertDialog() {
        return this.serverAlertDialog;
    }

    protected abstract void updateUserUI(boolean isLoggedIn);
    protected abstract void onLoggedIn();
    protected abstract void onLoggedOut();
}
