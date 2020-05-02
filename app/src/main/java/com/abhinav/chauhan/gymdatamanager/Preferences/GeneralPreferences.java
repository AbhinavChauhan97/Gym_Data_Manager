package com.abhinav.chauhan.gymdatamanager.Preferences;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.abhinav.chauhan.gymdatamanager.MyApplication;
import com.abhinav.chauhan.gymdatamanager.R;

import java.util.Objects;

public class GeneralPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("GENERAL SETTINGS");
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("SETTINGS");
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.general_preferences);
        getPreferenceScreen().getPreference(1).setOnPreferenceChangeListener((preference, newValue) -> {
            MyApplication.getInstance().setTheme((Integer.parseInt(newValue.toString())));
            return true;
        });
    }
}
