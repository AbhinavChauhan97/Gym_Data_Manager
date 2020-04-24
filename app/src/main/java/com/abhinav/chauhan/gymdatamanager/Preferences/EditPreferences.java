package com.abhinav.chauhan.gymdatamanager.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.abhinav.chauhan.gymdatamanager.R;

public class EditPreferences extends PreferenceFragmentCompat {
    private static EditPreferences editPreferences;

    public static EditPreferences getInstance() {
        if (editPreferences == null)
            editPreferences = new EditPreferences();
        return editPreferences;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    public SharedPreferences getUserPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
