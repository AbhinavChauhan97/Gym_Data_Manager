package com.abhinav.chauhan.gymdatamanager;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class MyApplication extends Application {

    public static MyApplication application = null;

    public static MyApplication getInstance() {
        if (application == null)
            application = new MyApplication();
        return application;
    }

    @Override
    public void onCreate() {
        int mode = Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("modes", "1"));
        AppCompatDelegate.setDefaultNightMode(mode);
        super.onCreate();
    }

    public void setTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
