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
public class ConnectivityReceiver extends BroadcastReceiver {
    private final String TAG = "ConnectivityReceiver";
    public static boolean NEED_USER_INFO = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "action: "+ intent.getAction());


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        LogInActivity.IS_NETWORK_AVAILABLE = this.haveNetworkConnection(context);

        if (LogInActivity.IS_NETWORK_AVAILABLE && ConnectivityReceiver.NEED_USER_INFO)
        {
            LogInActivity.setUserPersonName();
        }
    }

    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
