package com.abhinav.chauhan.gymdatamanager.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Fragments.MemberInfoFragment;
import com.abhinav.chauhan.gymdatamanager.Model.Member;


public class MemberInfoActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return MemberInfoFragment.newInstance((Member) getIntent().getSerializableExtra("Member"));
    }
}
