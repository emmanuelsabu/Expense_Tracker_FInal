package com.example.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class YearlyHomePage extends Fragment implements Animation.AnimationListener {
    FloatingActionButton add;
    int year;
    NumberPicker edityear;
    TextView income,expense,balance;
    IncomeDB mIncomeDB;

    int total_income=0;
    int total_expense=0;
    int total_balance;
    PieChartView piechartview;
    final List<SliceValue> piedata= new ArrayList<>();
    int i=0;
    SharedPreferences mSharedPreferences;
    String UniqueID;

    Animation mAnimation;
    ImageView zeroexpense;
    TextView zeroexpensetext;
    LinearLayout mLinearLayout;

    ArrayList<Values_Transaction> value_array = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.yearlyhomepage,container,false);
        zeroexpense=view.findViewById(R.id.zeroexpense);
        zeroexpensetext=view.findViewById(R.id.zeroexpensetext);
        edityear = view.findViewById(R.id.numberpicker2);
        add = view.findViewById(R.id.add2);
        income=view.findViewById(R.id.income2);
        income.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        expense=view.findViewById(R.id.expense2);
        expense.setTextColor(getActivity().getResources().getColor(R.color.red));
        balance=view.findViewById(R.id.balance2);
        mLinearLayout=view.findViewById(R.id.linearLayout);
        income.setKeyListener(null);
        expense.setKeyListener(null);
        balance.setKeyListener(null);

        mIncomeDB=new IncomeDB(getActivity());
        mSharedPreferences = this.getActivity().getSharedPreferences("Login Completed", Context.MODE_PRIVATE);

        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.slideup);
        mAnimation.setAnimationListener(this);
        add.startAnimation(mAnimation);
        mLinearLayout.startAnimation(mAnimation);

        piechartview =view.findViewById(R.id.chart2);
        piechartview.setViewportCalculationEnabled(true);
        piechartview.setCircleFillRatio((float) .9);


        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        edityear.setMinValue(2000);
        edityear.setMaxValue(this_year);
        edityear.setValue(this_year);
        edityear.setWrapSelectorWheel(false);
        year=edityear.getValue();

        edityear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year=edityear.getValue();
                calculation();
                calculation1();

            }
        });
        calculation();
        calculation1();



        add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        Intent intentAdd = new Intent(getActivity(),Add.class);
        startActivity(intentAdd);
        getActivity().finish();


    }
});
        return view;

    }
    public void calculation()
    {
        UniqueID=mSharedPreferences.getString("uniqueId","");
        String query = "Select * from IncomeDB where UniqueID ='"+UniqueID+"' and year ='"+year+"'";
        mIncomeDB.openconnection();
        total_income=0;
        total_expense=0;
        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0)
        {
            value_array.clear();
            while (cursor.moveToNext())
            {
                String UniqueId = cursor.getString(1);
                String type = cursor.getString(2);
                String category = cursor.getString(3);
                int cost = cursor.getInt(4);
                String date = cursor.getString(5);
                int day = cursor.getInt(6);
                int month = cursor.getInt(7);
                int year = cursor.getInt(8);

                value_array.add(new Values_Transaction(UniqueId,type,category,cost,date,day,month,year));

            }
            for (int i = 0;i<value_array.size();i++)
            {
            if(value_array.get(i).getType().equals("Income"))
            {
                total_income = total_income + value_array.get(i).getCost();
            }
            else
            {
                total_expense = total_expense + value_array.get(i).getCost();
            }
            }
        }
        total_balance = total_income-total_expense;
        income.setText(""+total_income);
        expense.setText(""+total_expense);
        balance.setText(""+total_balance);
        mIncomeDB.closeconnection();

        if (total_balance>0)
        {
            balance.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
        else if(total_balance<0)
        {
            balance.setTextColor(getActivity().getResources().getColor(R.color.red));
        }
        else
        {
            balance.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
    }
    public void calculation1()
    {
        UniqueID=mSharedPreferences.getString("uniqueId","");
        piedata.clear();

        int [] arraycolors = {getResources().getColor(R.color.orange),getResources().getColor(R.color.pink),getResources().getColor(R.color.aa), Color.MAGENTA,Color.GRAY,getResources().getColor(R.color.bb),Color.RED,getResources().getColor(R.color.blue),Color.LTGRAY,Color.GREEN,Color.BLUE,Color.DKGRAY,
                getResources().getColor(R.color.brown),getResources().getColor(R.color.purple)};
        String rtype="Expense";
        String query = "Select sum(cost),category from IncomeDB where UniqueID='"+UniqueID+"' and type ='" +rtype+"' and year ='"+year+"' group by category";
        mIncomeDB.openconnection();
        Cursor cursor = mIncomeDB.selectquery(query);
        if(cursor.getCount()>0) {
            value_array.clear();

            while (cursor.moveToNext()) {
                int cost = Integer.parseInt(cursor.getString(0));
                String category = cursor.getString(1);
                value_array.add(new Values_Transaction(category,cost,year));
                piedata.add(new SliceValue(cost,arraycolors[i]).setLabel(category+":"+cost));
                i++;
                if (i>arraycolors.length-1)
                {
                    i=0;
                }
            }
            mIncomeDB.closeconnection();
            piechartview.setVisibility(View.VISIBLE);
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            mAnimation.setAnimationListener(this);
            piechartview.startAnimation(mAnimation);
            zeroexpense.clearAnimation();
            zeroexpensetext.clearAnimation();
            zeroexpense.setVisibility(View.INVISIBLE);
            zeroexpensetext.setVisibility(View.INVISIBLE);
            PieChartData pieChartData = new PieChartData(piedata);
            pieChartData.setHasLabels(true).setValueLabelTextSize(10);
            pieChartData.setHasLabelsOutside(true);
            pieChartData.setHasCenterCircle(true);
            pieChartData .setCenterCircleColor(getResources().getColor(R.color.white));
            pieChartData.setCenterCircleScale((float) .5);
            pieChartData.setCenterText1("Expenses");
            pieChartData.setCenterText1Color(Color.BLACK);
            pieChartData.setCenterText1FontSize(15);
            pieChartData.setHasLabelsOnlyForSelected(true);
            piechartview.setPieChartData(pieChartData);
        }

        else
        {
            piechartview.clearAnimation();
            piechartview.setVisibility(View.INVISIBLE);
            zeroexpense.setVisibility(View.VISIBLE);
            zeroexpensetext.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.nodata).fit().into(zeroexpense);
            zeroexpensetext.setText(" Zero Expense");
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            mAnimation.setAnimationListener(this);
            zeroexpense.startAnimation(mAnimation);
            zeroexpensetext.startAnimation(mAnimation);
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
