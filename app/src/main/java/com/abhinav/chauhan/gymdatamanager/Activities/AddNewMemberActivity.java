package com.abhinav.chauhan.gymdatamanager.Activities;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.abhinav.chauhan.gymdatamanager.Fragments.AddNewMemberFragment;
import com.abhinav.chauhan.gymdatamanager.MyApplication;


public class AddNewMemberActivity extends SingleFragmentHost {

    @Override
    public Fragment createFragment() {
        return AddNewMemberFragment.newInstance();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MyApplication.getInstance().setLocale("hi", newBase);
    }
}

