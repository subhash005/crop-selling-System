package com.example.cropmanagmentsystem;

public class Cart_Item_model {
    private String cropId;
    private String cropName;
    private String cropImage;
    private int quantity;
    private double totalPrice;
    private double pricePerKg; // Added for consistency with logic

    public Cart_Item_model() {
        // Default constructor required for Firebase
    }

    public Cart_Item_model(String cropId, String cropName, String cropImage, int quantity, double totalPrice, double pricePerKg) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.cropImage = cropImage;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.pricePerKg = pricePerKg;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }
}