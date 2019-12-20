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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class TransactionYearly extends Fragment implements Animation.AnimationListener {
    IncomeDB mIncomeDB;
    RecyclerView mRecyclerView1;
    RecyclerAdapter1 recyclerAdapter1;
    ImageView empty;
    TextView empty_textview;
    ArrayList<Values_Transaction> value_array = new ArrayList<>();
    NumberPicker edityear;
    int year;
    SharedPreferences mSharedPreferences;
    String UniqueID;
    Calendar cal = Calendar.getInstance();
    Animation animBounce,animBounce1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.transactionyearly,container,false);

        mRecyclerView1 = view.findViewById(R.id.recyclerviewTransactionyearly);
        empty=view.findViewById(R.id.empty);
        empty_textview = view.findViewById(R.id.empty_text);
        edityear = view.findViewById(R.id.numberpicker6);
        mIncomeDB= new IncomeDB(getActivity());

        animBounce = AnimationUtils.loadAnimation(getContext(),R.anim.slidedown);
        animBounce.setAnimationListener(this);

        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        edityear.setMinValue(2000);
        edityear.setMaxValue(this_year);
        edityear.setValue(this_year);
        edityear.setWrapSelectorWheel(false);
        mSharedPreferences = this.getActivity().getSharedPreferences("Login Completed", Context.MODE_PRIVATE);

        year=edityear.getValue();
        getTransactiondata();
        mRecyclerView1.startAnimation(animBounce);

        edityear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year=edityear.getValue();
                getTransactiondata();
                mRecyclerView1.startAnimation(animBounce);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAdapter1 = new RecyclerAdapter1(getActivity(),value_array);
        mRecyclerView1.setLayoutManager(linearLayoutManager);
        mRecyclerView1.setAdapter(recyclerAdapter1);
        return view;
    }
    private void getTransactiondata()
    {
        UniqueID=mSharedPreferences.getString("uniqueId","");

        String query = "Select * from IncomeDB where UniqueID = '"+UniqueID+"' and year ='"+year+"' order by month DESC,day DESC";
        mIncomeDB.openconnection();
        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0)
        {
            value_array.clear();
            while (cursor.moveToNext())
            {   int id = cursor.getInt(0);
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
            mRecyclerView1.setAdapter(recyclerAdapter1);

        }
        else
        {
            value_array.clear();
            mRecyclerView1.setAdapter(recyclerAdapter1);
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
