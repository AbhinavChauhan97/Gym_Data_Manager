package com.bignerdranch.practice.Activities;

import androidx.fragment.app.Fragment;

import com.bignerdranch.practice.Preferences.EditPreferences;

public class PreferenceActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return new EditPreferences();
    }
}
