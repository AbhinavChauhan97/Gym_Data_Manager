package com.abhinav.chauhan.gymdatamanager.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Fragments.AddNewMemberFragment;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return AddNewMemberFragment.newInstance();
    }
}
