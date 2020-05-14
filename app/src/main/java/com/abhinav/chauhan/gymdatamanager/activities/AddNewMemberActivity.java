package com.abhinav.chauhan.gymdatamanager.activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.fragments.AddNewMemberFragment;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.add_member_activity);
        return AddNewMemberFragment.newInstance();
    }


}

