package com.abhinav.chauhan.gymdatamanager.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Preferences.EditPreferences;
import com.abhinav.chauhan.gymdatamanager.R;

public class PreferenceActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.settings);
        return new EditPreferences();
    }

}
