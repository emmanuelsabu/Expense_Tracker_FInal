package com.example.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class TransactionMonthly extends Fragment implements Animation.AnimationListener {
    IncomeDB mIncomeDB;
    RecyclerView mRecyclerView;
    RecyclerAdapter recyclerAdapter2;
    ImageView empty;
    TextView empty_textview;
    ArrayList<Values_Transaction> value_array = new ArrayList<>();
    Spinner mSpinner;
    NumberPicker edityear;
    String month_array[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String month;
    int year;
    int month_index;
    Calendar cal = Calendar.getInstance();
    SharedPreferences mSharedPreferences;
    String UniqueID;
    Animation animBounce,animBounce1;



    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactionmonthly,container,false);

        empty=view.findViewById(R.id.empty);
        empty_textview = view.findViewById(R.id.empty_text);
        mRecyclerView = view.findViewById(R.id.recyclerviewTransactionMonthly);
        mSpinner = view.findViewById(R.id.spinner);
        edityear = view.findViewById(R.id.numberpicker5);
        mIncomeDB= new IncomeDB(getActivity());
        mSharedPreferences = this.getActivity().getSharedPreferences("Login Completed", Context.MODE_PRIVATE);

        animBounce = AnimationUtils.loadAnimation(getContext(),R.anim.slidedown);
        animBounce.setAnimationListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAdapter2 = new RecyclerAdapter(getContext(),value_array);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter2);

        SimpleDateFormat month_date = new SimpleDateFormat("MM");
        int current_month = Integer.parseInt(month_date.format(cal.getTime()));

        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        edityear.setMinValue(2000);
        edityear.setMaxValue(this_year);
        edityear.setValue(this_year);
        edityear.setWrapSelectorWheel(false);
        year=edityear.getValue();

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_checked,month_array);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        mSpinner.setAdapter(monthAdapter);
        mSpinner.setSelection(current_month-1);
        month_index=current_month-1;

        getTransactiondata1();

        edityear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year=edityear.getValue();
                getTransactiondata1();
                mRecyclerView.startAnimation(animBounce);



            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                                               {
                                                   month_index = i;
                                                   month = month_array[i];
                                                   getTransactiondata1();
                                                   mRecyclerView.startAnimation(animBounce);


                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> adapterView)
                                               {
                                                   Toast.makeText(getActivity(),"Select Month",Toast.LENGTH_SHORT).show();
                                               }
                                           });


        return view;
    }

    public void getTransactiondata1()
    {
        UniqueID=mSharedPreferences.getString("uniqueId","");

        String query = "Select * from IncomeDB where UniqueID = '"+UniqueID+"' and month ='"+month_index+"'and year ='"+year+"' order by day DESC";
        mIncomeDB.openconnection();
        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0)
        {
            value_array.clear();
            while (cursor.moveToNext())
            {
                int id =cursor.getInt(0);
                String uniqueID= cursor.getString(1);
                String type = cursor.getString(2);
                String category = cursor.getString(3);
                int cost = cursor.getInt(4);
                String date = cursor.getString(5);
                int day = cursor.getInt(6);
                int month = cursor.getInt(7);
                int year = cursor.getInt(8);

                value_array.add(new Values_Transaction(id,uniqueID,type,category,cost,date,day,month,year));
            }
            empty.clearAnimation();
            empty_textview.clearAnimation();
            empty.setVisibility(View.INVISIBLE);
            empty_textview.setVisibility(View.INVISIBLE);
            mRecyclerView.setAdapter(recyclerAdapter2);

        }
        else
        {
            value_array.clear();
            mRecyclerView.setAdapter(recyclerAdapter2);
            empty.setVisibility(View.VISIBLE);
            empty_textview.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.empty).fit().into(empty);
            animBounce1 = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            animBounce1.setAnimationListener(this);
            empty.startAnimation(animBounce1);
            empty_textview.startAnimation(animBounce1);
        }
        mIncomeDB.closeconnection();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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
