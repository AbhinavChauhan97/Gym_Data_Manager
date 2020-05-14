package com.abhinav.chauhan.gymdatamanager.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.abhinav.chauhan.gymdatamanager.R;

import java.util.Objects;

import static com.abhinav.chauhan.gymdatamanager.MyContextWrapper.wrap;

public class SecurityPreferences extends PreferenceFragmentCompat {

    private SharedPreferences userPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (userPreferences.getBoolean("security", false)) {
            addSecurityPreferences();
        }
        getPreferenceScreen().getPreference(0).setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue) {
                addSecurityPreferences();
            } else {
                getPreferenceScreen().removePreference(getPreferenceScreen().getPreference(1));
            }
            return true;
        });
    }

    private void addSecurityPreferences() {
        SecurityOptionsPreference preference = new SecurityOptionsPreference(getContext());
        getPreferenceScreen().addPreference(preference);
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.security);
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.security_preferences);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getPreferenceScreen().getPreferenceCount() > 1) {
            getPreferenceScreen().removePreference(getPreferenceScreen().getPreference(1));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(wrap(context));
    }
}