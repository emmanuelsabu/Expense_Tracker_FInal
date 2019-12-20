package com.example.expensetracker;

public class Category_List
{
    String type,category;
    int id;

    public Category_List(int id,String type, String category) {
        this.type = type;
        this.category = category;
        this.id = id;
    }

    public Category_List(String type, String category) {

        this.type = type;
        this.category = category;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

