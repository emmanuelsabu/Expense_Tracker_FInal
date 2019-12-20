package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter_homepage extends FragmentPagerAdapter
{
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mTitle = new ArrayList<>();


    public ViewPagerAdapter_homepage(FragmentManager supportFragmentManager)
    {
        super(supportFragmentManager);

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
    public void addFrag(Fragment fragment,String Title)
    {
        mFragments.add(fragment);
        mTitle.add(Title);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);

    }

}
