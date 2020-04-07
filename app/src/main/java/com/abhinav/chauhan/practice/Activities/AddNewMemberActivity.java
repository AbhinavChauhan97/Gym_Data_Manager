package com.abhinav.chauhan.practice.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.practice.Fragments.AddNewMemberFragment;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return AddNewMemberFragment.newInstance();
    }
}
