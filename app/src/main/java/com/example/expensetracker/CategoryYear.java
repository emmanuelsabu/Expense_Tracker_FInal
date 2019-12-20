package com.example.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

public class CategoryYear extends Fragment
{
    NumberPicker edityear;
    IncomeDB mIncomeDB;
    RecyclerView mRecyclerView;
    CategoryYearRA recyclerAdapter;
    int year;
    String UniqueID;
    Calendar cal = Calendar.getInstance();
    SharedPreferences mSharedPreferences;



    ArrayList<Values_Transaction> value_array = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.categoryyear,container,false);

        edityear=view.findViewById(R.id.numberpicker4);
        mIncomeDB = new IncomeDB(getActivity());
        mRecyclerView=view.findViewById(R.id.recyclerview4);
        mSharedPreferences = this.getActivity().getSharedPreferences("Login Completed", Context.MODE_PRIVATE);


        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        edityear.setMinValue(2000);
        edityear.setMaxValue(this_year);
        edityear.setValue(this_year);
        edityear.setWrapSelectorWheel(false);
        year = edityear.getValue();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new CategoryYearRA(getActivity(),value_array);

        calculation();

        edityear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year = edityear.getValue();
                calculation();
            }
        });


        return view;
    }
    public void calculation()
    {
        String rtype="Expense";
        UniqueID=mSharedPreferences.getString("uniqueId","");
        String query = "Select sum(cost),category from IncomeDB where UniqueID ='"+UniqueID+"' and type ='" +rtype+"' and year ='"+year+"' group by category";
        mIncomeDB.openconnection();
        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0) {
            value_array.clear();
            while (cursor.moveToNext()) {
                String category = cursor.getString(1);
                int cost = cursor.getInt(0);
                value_array.add(new Values_Transaction(category, cost, year));
            }
            mIncomeDB.closeconnection();
        }

        else
        {
            Toast.makeText(getActivity(),"Empty Data",Toast.LENGTH_SHORT).show();
            value_array.clear();
        }
        mRecyclerView.setAdapter(recyclerAdapter);

    }
}
