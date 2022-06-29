package com.example.divicemonitor2.tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MonitorAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public void setFragments(List<Fragment> fragments){
        this.fragments=fragments;
    }
    public MonitorAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

}
