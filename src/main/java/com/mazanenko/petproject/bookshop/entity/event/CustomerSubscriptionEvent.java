package com.mazanenko.petproject.bookshop.entity.event;

public class CustomerSubscriptionEvent {
    private final String name;
    private final String customerEmail;
    private final Long productId;

    public CustomerSubscriptionEvent(String name, String customerEmail, Long productId) {
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

    public Long getProductId() {
        return productId;
    }
}
