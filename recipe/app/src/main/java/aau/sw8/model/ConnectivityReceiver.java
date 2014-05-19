package aau.sw8.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import aau.sw8.recipe.LogInActivity;

/**
 * Created by jacob on 5/16/14.
 */
public class ConnectivityReceiver {
    private static ConnectivityReceiver instance = new ConnectivityReceiver();
    ConnectivityManager connectivityManager;
    private final String TAG = "ConnectivityReceiver";
    public static boolean NEED_USER_INFO = false;
    static Context context;
    boolean connected = false;

    public static ConnectivityReceiver getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public boolean isNetworkConnected(Context con){
        try {
            connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
