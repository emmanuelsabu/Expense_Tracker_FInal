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

public class Subcategory extends AppCompatActivity implements Animation.AnimationListener {
    TextView t1,t2;
    RecyclerView mRecyclerView;
    IncomeDB mIncomeDB;
    String months[] = {"January","February","March","April","May","June","July","August","Septemper","October","November","December"};
    ArrayList<Values_Transaction> value_array = new ArrayList<>();
    SharedPreferences mSharedPreferences;
    String UniqueID;
    Animation animBounce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory);

        animBounce = AnimationUtils.loadAnimation(Subcategory.this,R.anim.slidedown);
        animBounce.setAnimationListener(this);

        t1=findViewById(R.id.t111);
        t2=findViewById(R.id.t222);
        mRecyclerView=findViewById(R.id.recyclerview333);
        mIncomeDB =new IncomeDB(Subcategory.this);
        mSharedPreferences = getSharedPreferences("Login Completed", Context.MODE_PRIVATE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Subcategory.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        final SubCategoryRecyclerAdapter recyclerAdapter = new SubCategoryRecyclerAdapter(Subcategory.this,value_array);
        mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView.startAnimation(animBounce);

        String category =getIntent().getExtras().getString("category");
        int month = getIntent().getExtras().getInt("month");
        int year = getIntent().getExtras().getInt("year");

        t1.setText("Month: "+months[month]);
        t2.setText("Year: "+year);
        UniqueID=mSharedPreferences.getString("uniqueId","");
        String query ="Select ID,category,cost,date from IncomeDB where UniqueID ='"+UniqueID+"' and category ='" +category+"' and month ='"+month+"'and year ='"+year+"' order by year,month,day DESC";
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
            Toast.makeText(Subcategory.this,"Empty",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Subcategory.this,Categorys.class);
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
