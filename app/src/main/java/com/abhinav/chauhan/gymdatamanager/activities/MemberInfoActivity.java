package com.abhinav.chauhan.gymdatamanager.activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.fragments.MemberInfoFragment;
import com.abhinav.chauhan.gymdatamanager.model.Member;


public class MemberInfoActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.member_info_activity);
        return MemberInfoFragment.newInstance((Member) getIntent().getSerializableExtra("Member"));
    }
}
