package com.abhinav.chauhan.gymdatamanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.abhinav.chauhan.gymdatamanager.R;
import com.abhinav.chauhan.gymdatamanager.activities.AddNewMemberActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainScreenViewPagerFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.entry_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(view);
        setAddMemberFab(view);
        //setupSnackBar(view);
    }

    private void setupViewPager(View view) {
        PagerTabStrip pagerTabStrip = view.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTextColor(getResources().getColor(android.R.color.white));
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.white));
        ViewPager mViewPager = view.findViewById(R.id.list);
        mViewPager.setAdapter(new FragmentPagerAdapter(requireActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return MembersNamesRecyclerViewFragment.newInstance();
                } else {
                    return AllMembersRecordsFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @NonNull
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) return getResources().getStringArray(R.array.view_pager_tabs)[0];
                else return getResources().getStringArray(R.array.view_pager_tabs)[1];
            }
        });
    }

    private void setupSnackBar(View view) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean b = connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        if (b) {
            Snackbar mobileDataBar = Snackbar.make(view, "Enable Internet For a Seamless Experience", BaseTransientBottomBar.LENGTH_LONG);
            mobileDataBar.setAction("ENABLE", v ->
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
            mobileDataBar.show();
        }
    }

    private void setAddMemberFab(View view) {
        FloatingActionButton mAddMemberFab;
        mAddMemberFab = view.findViewById(R.id.add_new_member_fab);
        mAddMemberFab.setOnClickListener(v -> startActivity(new Intent(MainScreenViewPagerFragment.this.getActivity(), AddNewMemberActivity.class)));

    }

}
