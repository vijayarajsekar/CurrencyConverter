package demo.currencyconverter;

import android.app.Application;

/**
 * Created by vijayaraj_s on 11/15/2017.
 */

public class Singleton extends Application {

    private static Singleton mInstance;

    private static AppPreference mAppPreference;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mAppPreference = new AppPreference(mInstance);
    }

    public static synchronized Singleton getInstance() {
        return mInstance;
    }

    public static synchronized AppPreference getPreferenceInstance() {
        return mAppPreference;
    }
}
