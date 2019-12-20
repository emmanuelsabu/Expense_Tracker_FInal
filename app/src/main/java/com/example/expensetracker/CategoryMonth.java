package com.example.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class CategoryMonth extends Fragment {
    Spinner mSpinner;
    NumberPicker edityear;
    IncomeDB mIncomeDB;
    String UniqueID;
    RecyclerView mRecyclerView;
    CategoryMonthRA recyclerAdapter;
    String month_array[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int month_index, year;
    String month;
    Calendar cal = Calendar.getInstance();
    SharedPreferences mSharedPreferences;
    ArrayList<Values_Transaction> value_array = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.categorymonth, container, false);
        mSpinner = view.findViewById(R.id.spinner3);
        edityear = view.findViewById(R.id.numberpicker3);
        mIncomeDB = new IncomeDB(getActivity());
        mRecyclerView = view.findViewById(R.id.recyclerview3);
        mSharedPreferences = this.getActivity().getSharedPreferences("Login Completed", Context.MODE_PRIVATE);


        SimpleDateFormat month_date = new SimpleDateFormat("MM");
        int current_month = Integer.parseInt(month_date.format(cal.getTime()));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new CategoryMonthRA(getActivity(), value_array);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_checked, month_array);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        mSpinner.setAdapter(monthAdapter);
        mSpinner.setSelection(current_month - 1);
        month_index=current_month-1;


        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        edityear.setMinValue(2000);
        edityear.setMaxValue(this_year);
        edityear.setValue(this_year);
        edityear.setWrapSelectorWheel(false);
        year = edityear.getValue();

        calculation();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month_index = i;
                month = month_array[i];
                calculation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Select Month", Toast.LENGTH_SHORT).show();
            }
        });

        edityear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year = edityear.getValue();
                calculation();

            }
        });

        return view;
    }

    public void calculation() {
        String rtype = "Expense";
        UniqueID=mSharedPreferences.getString("uniqueId","");

        String query = "Select sum(cost),category from IncomeDB where UniqueID ='"+UniqueID+"' and type ='" + rtype + "' and month ='" + month_index + "'and year ='" + year + "' group by category";
        mIncomeDB.openconnection();
        Cursor cursor = mIncomeDB.selectquery(query);
        if (cursor.getCount() > 0) {
            value_array.clear();
            while (cursor.moveToNext()) {
                int cost = Integer.parseInt(cursor.getString(0));
                String category = cursor.getString(1);
                value_array.add(new Values_Transaction(category, cost, month_index, year));

            }
            mIncomeDB.closeconnection();

        } else {
            Toast.makeText(getActivity(), "Empty Data", Toast.LENGTH_SHORT).show();
            value_array.clear();
            mIncomeDB.closeconnection();
        }
        mRecyclerView.setAdapter(recyclerAdapter);



    }
}
