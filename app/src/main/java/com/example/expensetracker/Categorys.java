package com.example.expensetracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Categorys extends AppCompatActivity
{
    TabLayout mTabLayout;
    ViewPager mViewPager;
    int index;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorys);

        mTabLayout = findViewById(R.id.tablayoutCategory);
        mViewPager = findViewById(R.id.viewpagerCategory);

        CategoryViewPagerAdapter adapter = new CategoryViewPagerAdapter (getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        addTabs(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(Color.BLACK,Color.WHITE);

        if (getIntent().getExtras()!=null)
        {
            if (getIntent().getExtras().getString("Fragments").equals("monthlycategory"))
            {
                index = 0;
            }
            else if (getIntent().getExtras().getString("Fragments").equals("yearlycategory"))
            {
                index = 1;
            }
            mViewPager.setCurrentItem(index);
        }

    }
    private void addTabs (ViewPager viewPager)
    {
        CategoryViewPagerAdapter adapter = new CategoryViewPagerAdapter(getSupportFragmentManager());
        adapter.addfrag(new CategoryMonth(), "Monthly");
        adapter.addfrag(new CategoryYear(), "Yearly");
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Categorys.this,HomePage.class);
        startActivity(intent);
    }
}
