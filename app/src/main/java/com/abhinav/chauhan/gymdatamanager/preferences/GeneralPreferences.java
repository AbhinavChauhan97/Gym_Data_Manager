package com.abhinav.chauhan.gymdatamanager.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.abhinav.chauhan.gymdatamanager.MyContextWrapper;
import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.activities.MainActivity;

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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.general);
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.settings);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyContextWrapper.wrap(context);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.general_preferences);
        getPreferenceScreen().getPreference(1).setOnPreferenceChangeListener((preference, newValue) -> {
            AppCompatDelegate.setDefaultNightMode(Integer.parseInt(newValue.toString()));
            return true;
        });
        ListPreference languages = (ListPreference) getPreferenceScreen().getPreference(2);
        languages.setOnPreferenceChangeListener(((preference, newValue) -> {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit()
                    .putString("lang", newValue.toString())
                    .commit();
            Log.d("db", String.valueOf(newValue.toString().length()));
            MyContextWrapper.wrap(getContext());
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }));
    }
}
