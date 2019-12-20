package com.example.expensetracker;

public class Values_Transaction
{
    String type,category,dates,UniqueID;
    int id,cost,day,month,year;

    public Values_Transaction(int id, String uniqueID, String type, String category, int cost, String dates, int day, int month, int year) {
        this.type = type;
        this.category = category;
        this.dates = dates;
        this.UniqueID = uniqueID;
        this.id = id;
        this.cost = cost;
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public Values_Transaction( String uniqueID, String type, String category, int cost, String dates, int day, int month, int year) {
        this.type = type;
        this.category = category;
        this.dates = dates;
        this.UniqueID = uniqueID;
        this.cost = cost;
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public Values_Transaction(int id, String type, String category, int cost, String dates, int day, int month, int year) {
        this.type = type;
        this.category = category;
        this.dates = dates;
        this.id = id;
        this.cost = cost;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Values_Transaction( int id,String category,  int cost,String dates) {
        this.category = category;
        this.dates = dates;
        this.id = id;
        this.cost = cost;
    }

    public Values_Transaction(String category, int cost, int month, int year) {
        this.category = category;
        this.cost = cost;
        this.month = month;
        this.year = year;
    }

    public Values_Transaction(String category,  int cost,String dates)
    {
        this.category = category;
        this.dates = dates;
        this.cost = cost;
    }

    public Values_Transaction(String category, int cost, int year) {
        this.category = category;
        this.cost = cost;
        this.year = year;
    }
    public Values_Transaction(String type, String category, int cost, String dates, int day, int month, int year) {
        this.type = type;
        this.category = category;
        this.cost = cost;
        this.dates = dates;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(String uniqueID) {
        UniqueID = uniqueID;
    }

}

