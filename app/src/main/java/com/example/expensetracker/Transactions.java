package com.example.expensetracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class Transactions extends AppCompatActivity
{
    TabLayout mTabLayout;
    ViewPager mViewPager;
    int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);


        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        addTabs(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(Color.BLACK,Color.WHITE);

        if (getIntent().getExtras().getString("Fragments")!=null)
        {
            if (getIntent().getExtras().getString("Fragments").equals("dailytransaction"))
            {
                index = 0;
            }
            else if (getIntent().getExtras().getString("Fragments").equals("monthlytransaction"))
            {
                index = 1;
            }
            else if (getIntent().getExtras().getString("Fragments").equals("yearlytransaction"))
            {
                index = 2;
            }
            mViewPager.setCurrentItem(index);

        }
        else if (getIntent().getExtras().get("Fragment")!=null)
        {

                index = getIntent().getExtras().getInt("Fragment");

            mViewPager.setCurrentItem(index);
        }
    }
    private void addTabs(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TransactionDaily(),"Daily");
        adapter.addFrag(new TransactionMonthly(),"Monthly");
        adapter.addFrag(new TransactionYearly(),"Yearly");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Transactions.this,HomePage.class);
        startActivity(intent);
    }
}
