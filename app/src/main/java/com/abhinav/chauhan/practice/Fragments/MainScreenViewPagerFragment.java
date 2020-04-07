package com.abhinav.chauhan.practice.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.abhinav.chauhan.practice.Activities.AddNewMemberActivity;
import com.abhinav.chauhan.practice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainScreenViewPagerFragment extends Fragment {

    ViewPager mViewPager;

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
    }

    private void setupViewPager(View view) {
        PagerTabStrip pagerTabStrip = view.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTextColor(getResources().getColor(android.R.color.white));
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.white));
        mViewPager = view.findViewById(R.id.list);
        mViewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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

    private void setAddMemberFab(View view) {
        FloatingActionButton mAddMemberFab;
        mAddMemberFab = view.findViewById(R.id.add_new_member_fab);
        mAddMemberFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenViewPagerFragment.this.getActivity(), AddNewMemberActivity.class));
            }
        });
    }
}
