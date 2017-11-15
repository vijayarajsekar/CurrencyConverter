package demo.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vijayaraj_s on 11/15/2017.
 */

public class AppPreference {

    // Shared Preferences
    private final SharedPreferences mPreferences;

    // Editor for Shared preferences
    private final SharedPreferences.Editor mEditor;

    private String themeType = "themeType";

    private String historyData = "historyData";

    public AppPreference(Context mContext) {
        mPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preferenceName), 0);
        mEditor = mPreferences.edit();
    }

    public void setTheme(boolean _type) {

        mEditor.putBoolean(themeType, _type);
        mEditor.commit();
    }

    public boolean getTheme() {
        return mPreferences.getBoolean(themeType, false);
    }

    public void setHistory(String _value) {
        mEditor.putString(historyData, _value);
        mEditor.commit();
    }

    public String getHistory() {
        return mPreferences.getString(historyData, "");
    }
}
