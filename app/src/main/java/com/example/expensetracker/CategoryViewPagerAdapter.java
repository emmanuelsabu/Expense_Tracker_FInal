package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class CategoryViewPagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mTitle = new ArrayList<>();

    public CategoryViewPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addfrag(Fragment fragment,String title)
    {
        mFragments.add(fragment);
        mTitle.add(title);
    }
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }

}

