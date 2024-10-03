package com.example.cropmanagmentsystem;

public class Category {
    String categoryId , categoryName;

    public Category(String categoryId ,String categoryName){
        this.categoryId=categoryId;
        this.categoryName=categoryName;
    }

    public Category(){

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
