package com.bignerdranch.practice.Activities;

import androidx.fragment.app.Fragment;

import com.bignerdranch.practice.Fragments.AddNewMemberFragment;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return AddNewMemberFragment.newInstance();
    }
}
