package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments;
    private final ArrayList<String> titles;

    public PagerAdapter(FragmentManager fm){
        super(fm);
        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title){
        this.fragments.add(fragment);
        this.titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
