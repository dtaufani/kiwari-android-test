package com.taufani.kiwari.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dtaufani@gmail.com on 01/11/19.
 */

public class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    public SessionManager(Context ctx) {
        Context context = ctx;
        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public void putSharedPref(String key, String value) {
        this.mPref.edit().putString(key, value).commit();
    }

    public String getSharedPref(String key) {
        return this.mPref.getString(key, "");
    }

    public void removeSharedPref(String key) {
        this.mPref.edit().remove(key).commit();
    }

    public void clearSharedPrefs(){
        this.mPref.edit().clear().commit();
    }

    public boolean containsKey(String key){
        return this.mPref.contains(key);
    }

    public void setLogin(boolean isLoggedIn) {
        mEditor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        mEditor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return mPref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
