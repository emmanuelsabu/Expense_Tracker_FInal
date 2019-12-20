package com.example.expensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CategoryDB extends SQLiteOpenHelper
{
    SQLiteDatabase db;
    public CategoryDB(@Nullable Context context)
    {
        super(context, "CategoryDBExpense", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create Table CategoryDB(ID integer primary key , category text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openconnection()
    {
        db =getWritableDatabase();
    }
    public void closeconnection()
    {
        db.close();
    }

    Boolean executequery(String query)
    {
        try
        {
            db.execSQL(query);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    Cursor selectquery (String query)
    {
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
}


