package com.example.cropmanagmentsystem;

public class CartItem {
    private String cropName, cropImage;
    private int quantity, pricePerKg, totalPrice;

    public CartItem() {}

    public CartItem(String cropName, String cropImage, int quantity, int pricePerKg, int totalPrice) {
        this.cropName = cropName;
        this.cropImage = cropImage;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.totalPrice = totalPrice;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(int pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
