package com.example.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Delete_Category extends AppCompatActivity
{
    TextView type;
    RecyclerView mRecyclerView;

    CategoryDB mCategoryDB;
    CategoryDBExpense mCategoryDBExpense;
    ArrayList <Category_List> category_list = new ArrayList<>();

    Delete_Category_RecylerAdapter mDelete_category_recylerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_category);

        type = findViewById(R.id.delete_text);
        mRecyclerView = findViewById(R.id.recyclerVIew_category);
        mCategoryDB = new CategoryDB(Delete_Category.this);
        mCategoryDBExpense = new CategoryDBExpense(Delete_Category.this);


        int a = getIntent().getExtras().getInt("Type");

        if(a==1)
        {
            type.setText("Income Category");
            type.setTextColor (getResources().getColor(R.color.green));
            String query1 = "Select * from CategoryDB order by category ASC";
            mCategoryDB.openconnection();
            Cursor cursor = mCategoryDB.selectquery(query1);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                String newCategoryValue = cursor.getString(1);
                String type = "Income";
                int id = cursor.getInt(0);
                    category_list.add(new Category_List(id,type,newCategoryValue));
            }
            }

        }
        else if(a==2)
        {
            type.setText("Expense Category");
            type.setTextColor (getResources().getColor(R.color.red));
            String query1 = "Select * from CategoryDBExpense order by category ASC";
            mCategoryDBExpense.openconnection();
            Cursor cursor = mCategoryDBExpense.selectquery(query1);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext()) {
                    String newCategoryValue = cursor.getString(1);
                    String type = "Expense";
                    int id = cursor.getInt(0);
                    category_list.add(new Category_List(id,type,newCategoryValue));
                }
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Delete_Category.this);
        mDelete_category_recylerAdapter = new Delete_Category_RecylerAdapter(Delete_Category.this,category_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDelete_category_recylerAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Delete_Category.this,Add.class);
        startActivity(intent);
    }
}
