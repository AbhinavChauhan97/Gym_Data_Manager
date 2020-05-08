package com.abhinav.chauhan.gymdatamanager.Activities;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Fragments.AddNewMemberFragment;
import com.abhinav.chauhan.gymdatamanager.R;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.add_member_activity);
        return AddNewMemberFragment.newInstance();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}

