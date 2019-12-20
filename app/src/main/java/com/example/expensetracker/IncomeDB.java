package com.example.expensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class IncomeDB extends SQLiteOpenHelper
{
    SQLiteDatabase sqLiteDatabase;
    public IncomeDB(@Nullable Context context)
    {
        super(context, "IncomeDB", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("Create table IncomeDB (ID integer primary key,UniqueID text, type text,category text,cost integer,date text,day number,month number,year number)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void openconnection()
    {
        sqLiteDatabase =getWritableDatabase();
    }
    public void closeconnection()
    {
        sqLiteDatabase.close();
    }

    Boolean executequery(String query)
    {
        try
        {
            sqLiteDatabase.execSQL(query);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    Cursor selectquery (String query)
    {
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }
}
