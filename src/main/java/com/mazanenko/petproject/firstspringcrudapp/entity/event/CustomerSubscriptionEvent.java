package com.mazanenko.petproject.firstspringcrudapp.entity.event;

public class CustomerSubscriptionEvent {
    private final String name;
    private final String customerEmail;
    private final int productId;

    public CustomerSubscriptionEvent(String name, String customerEmail, int productId) {
        this.name = name;
        this.customerEmail = customerEmail;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getProductId() {
        return productId;
    }
}
