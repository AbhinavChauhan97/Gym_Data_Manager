package com.bignerdranch.practice.Activities;

import androidx.fragment.app.Fragment;

import com.bignerdranch.practice.Fragments.MemberInfoFragment;
import com.bignerdranch.practice.Model.Member;


public class MemberInfoActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return MemberInfoFragment.newInstance((Member) getIntent().getSerializableExtra("Member"));
    }
}
