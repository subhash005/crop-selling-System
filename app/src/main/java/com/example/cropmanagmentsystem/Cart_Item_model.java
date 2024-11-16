package com.example.cropmanagmentsystem;

public class Cart_Item_model {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private String cropId, cropName, cropImage;
    private int quantity;
    private double totalPrice;

    public void CartModel() {}

    public void CartModel(String cropId, String cropName, String cropImage, int quantity, double totalPrice) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.cropImage = cropImage;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
