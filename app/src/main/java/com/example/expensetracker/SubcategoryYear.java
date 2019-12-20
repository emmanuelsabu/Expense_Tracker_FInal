package com.example.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class SubcategoryYear extends AppCompatActivity implements Animation.AnimationListener {
    TextView t1;
    RecyclerView mRecyclerView;
    IncomeDB mIncomeDB;
    ArrayList<Values_Transaction> value_array = new ArrayList<>();
    SharedPreferences mSharedPreferences;
    String UniqueID;
    Animation animBounce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategoryyear);

        animBounce = AnimationUtils.loadAnimation(SubcategoryYear.this,R.anim.slidedown);
        animBounce.setAnimationListener(this);


        t1=findViewById(R.id.y1);
        mRecyclerView=findViewById(R.id.y2);
        mIncomeDB =new IncomeDB(SubcategoryYear.this);
        mSharedPreferences = getSharedPreferences("Login Completed", Context.MODE_PRIVATE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubcategoryYear.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final SubCategoryYearRecyclerAdapter recyclerAdapter = new SubCategoryYearRecyclerAdapter(SubcategoryYear.this,value_array);
        mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView.startAnimation(animBounce);
        String category =getIntent().getExtras().getString("category");
        int year = getIntent().getExtras().getInt("year");

        t1.setText("Year: "+year);
        UniqueID=mSharedPreferences.getString("uniqueId","");
        String query ="Select ID,category,cost,date from IncomeDB where UniqueID ='"+UniqueID+"' and category ='" +category+"' and year ='"+year+"' order by month DESC, day DESC";
        mIncomeDB.openconnection();

        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int id= cursor.getInt(0);
                String subcategory = cursor.getString(1);
                int cost = cursor.getInt(2);
                String date = cursor.getString(3);

                value_array.add(new Values_Transaction(id,subcategory,cost,date));

            }
        }
        else
        {
            Toast.makeText(SubcategoryYear.this,"Empty",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SubcategoryYear.this,Categorys.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
