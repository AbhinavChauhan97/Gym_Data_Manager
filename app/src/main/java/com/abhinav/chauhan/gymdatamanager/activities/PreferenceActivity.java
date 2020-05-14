package com.abhinav.chauhan.gymdatamanager.activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.preferences.EditPreferences;

public class PreferenceActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.settings);
        return new EditPreferences();
    }

}
