package com.example.cropmanagmentsystem;

public class crops_model {
    String crop_pic;
    String crop_name;
    String crop_description;
    String crop_category;
    boolean crop_isOrganic;
    int crop_stock;
    double crop_price;

    public crops_model(String crop_pic, String crop_name, String crop_description, boolean crop_isOrganic, String crop_category, int crop_stock, double crop_price) {
        this.crop_pic = crop_pic;
        this.crop_name = crop_name;
        this.crop_description = crop_description;
        this.crop_isOrganic = crop_isOrganic;
        this.crop_category = crop_category;
        this.crop_stock = crop_stock;
        this.crop_price = crop_price;
    }

    public crops_model() {
    }

    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public String getCrop_pic() {
        return crop_pic;
    }

    public void setCrop_pic(String crop_pic) {
        this.crop_pic = crop_pic;
    }

    public String getCrop_description() {
        return crop_description;
    }

    public void setCrop_description(String crop_description) {
        this.crop_description = crop_description;
    }

    public String getCrop_category() {
        return crop_category;
    }

    public void setCrop_category(String crop_category) {
        this.crop_category = crop_category;
    }

    public boolean isCrop_isOrganic() {
        return crop_isOrganic;
    }

    public void setCrop_isOrganic(boolean crop_isOrganic) {
        this.crop_isOrganic = crop_isOrganic;
    }

    public int getCrop_stock() {
        return crop_stock;
    }

    public void setCrop_stock(int crop_stock) {
        this.crop_stock = crop_stock;
    }

    public double getCrop_price() {
        return crop_price;
    }

    public void setCrop_price(double crop_price) {
        this.crop_price = crop_price;
    }
}
