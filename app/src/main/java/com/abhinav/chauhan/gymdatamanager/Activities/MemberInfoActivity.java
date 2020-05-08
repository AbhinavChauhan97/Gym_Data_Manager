package com.abhinav.chauhan.gymdatamanager.Activities;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Fragments.MemberInfoFragment;
import com.abhinav.chauhan.gymdatamanager.Model.Member;
import com.abhinav.chauhan.gymdatamanager.R;


public class MemberInfoActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        setTitle(R.string.member_info_activity);
        return MemberInfoFragment.newInstance((Member) getIntent().getSerializableExtra("Member"));
    }
}
