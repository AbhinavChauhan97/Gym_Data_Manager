package com.abhinav.chauhan.gymdatamanager.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Preferences.EditPreferences;

public class PreferenceActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return new EditPreferences();
    }

}
