package com.abhinav.chauhan.practice.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.abhinav.chauhan.practice.R;

public class EditPreferences extends PreferenceFragmentCompat {
    private static EditPreferences editPreferences;
    private String MY_PREFS = "com.goldy.myprefs";
    private String FEES = "com.goldy.fees";

    public static EditPreferences getInstance() {
        if (editPreferences == null)
            editPreferences = new EditPreferences();
        return editPreferences;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    public SharedPreferences getUserPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
