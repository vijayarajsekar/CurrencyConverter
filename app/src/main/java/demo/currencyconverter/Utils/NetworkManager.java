package demo.currencyconverter.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import demo.currencyconverter.Singleton;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class NetworkManager {

    private static String TAG = NetworkManager.class.getSimpleName();

    public static boolean isNetworkAvailable() {

        try {
            ConnectivityManager cm = (ConnectivityManager) Singleton.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage().toString());
            return false;
        }
    }
}
