package com.example.expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Add extends AppCompatActivity
{
    RadioGroup mRadioGroup;
    RadioButton income,expense;
    Spinner categories_spinner;
    EditText datepicker;
    Button add;
    FloatingActionButton addCategory,deleteCategory;
    final  Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendar = Calendar.getInstance();
    String category, dates;
    String type, UniqueID;
    SharedPreferences mSharedPreferences;
    EditText amount;
    int cost;
    int day1, month1, year1,day2,month2,year2;
    IncomeDB mIncomeDB;
    CategoryDB mCategoryDB;
    CategoryDBExpense mCategoryDBExpense;
    ArrayList<String> category_array = new ArrayList<>();
    ArrayList<String> category_array_Income = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpage);

        mRadioGroup = findViewById(R.id.radioGroup);
        income = findViewById(R.id.income);
        expense = findViewById(R.id.expense);
        categories_spinner = findViewById(R.id.categories);
        datepicker = findViewById(R.id.datepicker);
        amount = findViewById(R.id.amount1);
        int maxLength = 10;
        amount.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});


        add=findViewById(R.id.add);
        addCategory=findViewById(R.id.addcategory);
        deleteCategory = findViewById(R.id.deletecategory);
        datepicker = findViewById(R.id.datepicker);

        mCategoryDB = new CategoryDB(Add.this);
        mCategoryDBExpense = new CategoryDBExpense(Add.this);
        mIncomeDB = new IncomeDB(Add.this);
        mSharedPreferences = Add.this.getSharedPreferences("Login Completed", Context.MODE_PRIVATE);


        expense.setChecked(true);
        int id=mRadioGroup.getCheckedRadioButtonId();
        switch (id)
        {
            case R.id.income:
            {
                addIncome();
            }
            case R.id.expense:
            {
                addexpense();

            }
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int id=mRadioGroup.getCheckedRadioButtonId();

                if(id==R.id.income)
                {
                    addIncome();
                }
                else
                {
                    addexpense();
                }
            }
        });

    }
    public void addIncome()
    {


        spinnercategoryIncome();
        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                category = category_array_Income.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                Toast.makeText(Add.this,"Select Category",Toast.LENGTH_SHORT).show();
            }
        });

        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datepicker.setText(sdf.format(myCalendar.getTime()));
        day2=myCalendar.get(Calendar.DAY_OF_MONTH);
        month2 =myCalendar.get(Calendar.MONTH);
        year2=myCalendar.get(Calendar.YEAR);
        dates = String.valueOf(datepicker.getText());

        final long a = myCalendar.getTimeInMillis();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                long b = myCalendar.getTimeInMillis();

                if(a>=b)
                {
                    day2 = dayOfMonth;
                    month2 = monthOfYear;
                    year2 = year;

                    updateLabel();
                }
                else
                {
                    Toast.makeText(Add.this,"Cannot be Selected",Toast.LENGTH_SHORT).show();
                }
            }
        };
        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategoryIncome();
            }
        });
        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_delete = new Intent(Add.this,Delete_Category.class);
                intent_delete.putExtra("Type",1);
                startActivity(intent_delete);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cash = amount.getText().toString();
                if (cash.equals("")) {
                    Toast.makeText(Add.this, "Enter the Amount", Toast.LENGTH_SHORT).show();
                }
                else {
                    cost = Integer.parseInt(cash);
                    type = "Income";
                    UniqueID = mSharedPreferences.getString("uniqueId", "");

                    String query = "insert into IncomeDB(UniqueID,type,category,cost,date,day,month,year) values" +
                            " ('" + UniqueID + "','" + type + "','" + category + "','" + cost + "','" + dates + "','" + day2 + "','" + month2 + "','" + year2 + "')";
                    mIncomeDB.openconnection();
                    Boolean bool = mIncomeDB.executequery(query);
                    if (bool) {
                        Toast.makeText(Add.this, "Income Inserted", Toast.LENGTH_SHORT).show();
                        spinnercategoryIncome();
                    }
                    else {
                        Toast.makeText(Add.this, "Income not Inserted", Toast.LENGTH_SHORT).show();
                    }
                    mIncomeDB.closeconnection();
                    Intent intent3 = new Intent(Add.this, HomePage.class);
                    finish();
                    startActivity(intent3);

                }
            }
        });

    }

    public void addexpense()
    {
        spinnercategoryExpense();

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                category = category_array.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Add.this, "Select Category", Toast.LENGTH_SHORT).show();
            }
        });

        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datepicker.setText(sdf.format(myCalendar1.getTime()));
        day1 = myCalendar1.get(Calendar.DAY_OF_MONTH);
        month1 = myCalendar1.get(Calendar.MONTH);
        year1 = myCalendar1.get(Calendar.YEAR);
        dates = String.valueOf(datepicker.getText());

        final long a = myCalendar1.getTimeInMillis();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                final long b = myCalendar1.getTimeInMillis();

                if(a>=b)
                {
                    day1 = dayOfMonth;
                    month1 = monthOfYear;
                    year1 = year;
                    updateLabel1();
                }
                else
                {
                    Toast.makeText(Add.this,"Cannot be Selected",Toast.LENGTH_SHORT).show();
                }
            }
        };

        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategoryExpense();
            }
        });
        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent_delete = new Intent(Add.this,Delete_Category.class);
                intent_delete.putExtra("Type",2);
                startActivity(intent_delete);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cash = amount.getText().toString();
                if (cash.equals("")) {
                    Toast.makeText(Add.this, "Enter the Amount", Toast.LENGTH_SHORT).show();
                } else {
                    cost = Integer.parseInt(cash);

                    type = "Expense";
                    UniqueID = mSharedPreferences.getString("uniqueId", "");
                    String query = "insert into IncomeDB(UniqueID,type,category,cost,date,day,month,year) " +
                            "values ('" + UniqueID + "','" + type + "','" + category + "','" + cost + "','" + dates + "','" + day1 + "','" + month1 + "','" + year1 + "')";

                    mIncomeDB.openconnection();
                    Boolean bool = mIncomeDB.executequery(query);
                    if (bool) {
                        Toast.makeText(Add.this, "Expense Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Add.this, "Expense not Inserted", Toast.LENGTH_SHORT).show();
                    }
                    mIncomeDB.closeconnection();
                    Intent intent2 = new Intent(Add.this, HomePage.class);
                    finish();
                    startActivity(intent2);
                }
            }
        });
    }


    private void spinnercategoryIncome()
    {
        category_array_Income.clear();
        category_array_Income.add("SALARY");
        category_array_Income.add("PROPERTY");
        category_array_Income.add("OTHER SOURCES");
        String query1 = "Select * from CategoryDB order by category ASC";
        mCategoryDB.openconnection();
        Cursor cursor = mCategoryDB.selectquery(query1);
        if (cursor.getCount() > 0)
        {
            category_array_Income.clear();
            category_array_Income.add("SALARY");
            category_array_Income.add("PROPERTY");
            category_array_Income.add("OTHER SOURCES");


            while (cursor.moveToNext()) {
                String newCategoryValue = cursor.getString(1);
                category_array_Income.add(newCategoryValue);
            }
        }
        else
        {
            Toast.makeText(Add.this,"Create Category",Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(Add.this, android.R.layout.simple_list_item_checked, category_array_Income);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        categories_spinner.setAdapter(categoryAdapter);
    }

    private void createCategoryIncome()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Add.this);

        View view= LayoutInflater.from(Add.this).inflate(R.layout.alert_layout,null);
        mBuilder.setView(view);
        final EditText input = view.findViewById(R.id.newCategory);
        input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        mBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_category_value = String.valueOf(input.getText());
                if (new_category_value.equals(""))
                {
                    Toast.makeText(Add.this, "Text Field Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (category_array_Income.contains(new_category_value)) {
                        Toast.makeText(Add.this, "Category already Present", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String query = "Insert into CategoryDB(category) values ('" + new_category_value + "')";
                        mCategoryDB.openconnection();
                        Boolean bool = mCategoryDB.executequery(query);
                        if (bool) {
                            category_array_Income.add(new_category_value);
                            Toast.makeText(Add.this, "Category Created", Toast.LENGTH_SHORT).show();

                            spinnercategoryIncome();

                        } else {
                            Toast.makeText(Add.this, "Category not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                mCategoryDB.closeconnection();
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.setTitle("ADD NEW CATEGORY");
        alertDialog.show();
    }

    private void spinnercategoryExpense()
    {
        category_array.clear();
        category_array.add("FOOD");
        category_array.add("RENT");
        category_array.add("TRAVEL");
        category_array.add("OTHER");

        String query1 = "Select * from CategoryDBExpense order by category ASC";
        mCategoryDBExpense.openconnection();
        Cursor cursor = mCategoryDBExpense.selectquery(query1);
        if (cursor.getCount() > 0) {
            category_array.clear();
            category_array.add("FOOD");
            category_array.add("RENT");
            category_array.add("TRAVEL");
            category_array.add("OTHER");

            while (cursor.moveToNext()) {
                String newCategoryValue = cursor.getString(1);
                category_array.add(newCategoryValue);
            }
        }


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(Add.this, android.R.layout.simple_list_item_checked, category_array);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        categories_spinner.setAdapter(categoryAdapter);
    }

    private void createCategoryExpense()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Add.this);

        View view= LayoutInflater.from(Add.this).inflate(R.layout.alert_layout,null);
        mBuilder.setView(view);
        final EditText input = view.findViewById(R.id.newCategory);
        input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        mBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_category_value = String.valueOf(input.getText());
                if (new_category_value.equals(""))
                {
                    Toast.makeText(Add.this, "Text Field Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (category_array.contains(new_category_value)) {
                        Toast.makeText(Add.this, "Category already present", Toast.LENGTH_SHORT).show();
                    } else {
                        String query = "Insert into CategoryDBExpense(category) values ('" + new_category_value + "')";
                        mCategoryDBExpense.openconnection();
                        Boolean bool = mCategoryDBExpense.executequery(query);
                        if (bool) {
                            category_array.add(new_category_value);
                            Toast.makeText(Add.this, "Category Created", Toast.LENGTH_SHORT).show();
                            spinnercategoryExpense();
                        } else {
                            Toast.makeText(Add.this, "Category not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                mCategoryDBExpense.closeconnection();
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.setTitle("ADD NEW CATEGORY");
        alertDialog.show();
    }

    private void updateLabel() {
        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datepicker.setText(sdf.format(myCalendar.getTime()));
        dates = String.valueOf(datepicker.getText());
    }

    private void updateLabel1() {
        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datepicker.setText(sdf.format(myCalendar1.getTime()));
        dates = String.valueOf(datepicker.getText());
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Add.this,HomePage.class);
        startActivity(intent1);
        finish();
    }
}