package com.abhinav.chauhan.gymdatamanager.preferences;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.abhinav.chauhan.gymdatamanager.MyContextWrapper;
import com.abhinav.chauhan.gymdatamanager.R;

public class EditPreferences extends PreferenceFragmentCompat {

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(MyContextWrapper.wrap(context));

    }
}
