package com.abhinav.chauhan.practice.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.practice.Preferences.EditPreferences;

public class PreferenceActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return new EditPreferences();
    }
}
