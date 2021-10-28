package com.mazanenko.petproject.firstspringcrudapp.entity;

public class Subscription {
    private int productId;
    private int customerId;

    public Subscription() {
    }

    public Subscription(int productId, int customerId) {
        this.productId = productId;
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
