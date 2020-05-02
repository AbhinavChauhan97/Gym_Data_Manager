package com.abhinav.chauhan.gymdatamanager;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.abhinav.chauhan.gymdatamanager.Preferences.EditPreferences;

import java.util.Locale;

public class MyApplication extends Application {

    public static MyApplication application = null;
    Locale locale;

    public static MyApplication getInstance() {
        if (application == null)
            application = new MyApplication();
        return application;
    }

    @Override
    public void onCreate() {
        String lang = EditPreferences.getInstance()
                .getUserPreference(getApplicationContext())
                .getString("lang", "en");
        //setLocale(lang);
        int mode = Integer.parseInt(EditPreferences.getInstance()
                .getUserPreference(getApplicationContext())
                .getString("modes", "1"));
        AppCompatDelegate.setDefaultNightMode(mode);
        super.onCreate();
    }

    public void setTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public Context setLocale(String lang, Context context) {
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(setLocale("hi", base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("hi", this);
    }
}
