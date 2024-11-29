package com.example.cropmanagmentsystem;

public class AdminCartItem {
    private String cropImage, cropName;
    private int pricePerKg, quantity, totalPrice;

    // Constructors, getters, setters


    public AdminCartItem() {
    }

    public AdminCartItem(String cropImage, String cropName, int pricePerKg, int quantity, int totalPrice) {
        this.cropImage = cropImage;
        this.cropName = cropName;
        this.pricePerKg = pricePerKg;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(int pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
