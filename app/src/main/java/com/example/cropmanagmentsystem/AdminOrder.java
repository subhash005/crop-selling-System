package com.example.cropmanagmentsystem;

import java.util.List;

public class AdminOrder {
    private String orderID, userId, userName, userNumber, deliveryAddress, paymentMethod, paymentStatus, deliveryStatus;
    private long orderDateTime;
    private int totalAmount;
    private List<AdminCartItem> cartItems;
    private String paymentID;
    private String paymentDateTime;
    private String orderMonth;
    private String orderYear;

    // Constructors, getters, setters

    public AdminOrder() {
    }

    public AdminOrder(String orderID, String userId, String userName, String userNumber, String deliveryAddress, String paymentMethod, String paymentStatus, String deliveryStatus, long orderDateTime, int totalAmount, List<AdminCartItem> cartItems, String paymentID, String paymentDateTime, String orderMonth, String orderYear) {
        this.orderID = orderID;
        this.userId = userId;
        this.userName = userName;
        this.userNumber = userNumber;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
        this.orderDateTime = orderDateTime;
        this.totalAmount = totalAmount;
        this.cartItems = cartItems;
        this.paymentID = paymentID;
        this.paymentDateTime = paymentDateTime;
        this.orderMonth = orderMonth;
        this.orderYear = orderYear;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public long getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(long orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<AdminCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<AdminCartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public String getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(String orderYear) {
        this.orderYear = orderYear;
    }
}

