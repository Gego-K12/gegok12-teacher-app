package com.gegosoft.yourappname.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetailsSharedPref {


    public static final String PREFS = "prefs";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static UserDetailsSharedPref instance = null;
    SharedPreferences.Editor editor;
    public SharedPreferences signup_pref;

    public SharedPreferences getPref() {
        return signup_pref;
    }

    public UserDetailsSharedPref(Context context) {
        this.signup_pref = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static UserDetailsSharedPref getInstance(Context context) {
        if (instance == null)
            instance = new UserDetailsSharedPref(context);
        return instance;
    }

    public void createLoginSession() {
        editor = signup_pref.edit();
        editor.putBoolean(IS_LOGIN, true);

        editor.commit();
    }

    public boolean isLoggedIn() {
        return signup_pref.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor = signup_pref.edit();
        editor.clear();
        editor.putString("firebasetoken", getString("firebasetoken"));
        editor.commit();
    }

    public void saveString(String key, String value) {
        this.signup_pref.edit().putString(key, value).commit();
    }

    public String getString(String Key) {
        return this.signup_pref.getString(Key, null);
    }

    public void saveBoolean(String key, Boolean value) {
        this.signup_pref.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.signup_pref.getBoolean(key, defaultValue);
    }

}

