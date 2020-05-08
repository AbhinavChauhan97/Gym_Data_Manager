package com.abhinav.chauhan.gymdatamanager;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    private MyContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context) {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("lang", "en");
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        return new MyContextWrapper(context);
    }

    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }
}
