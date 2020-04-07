package com.abhinav.chauhan.practice.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.practice.Fragments.MemberInfoFragment;
import com.abhinav.chauhan.practice.Model.Member;


public class MemberInfoActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return MemberInfoFragment.newInstance((Member) getIntent().getSerializableExtra("Member"));
    }
}
