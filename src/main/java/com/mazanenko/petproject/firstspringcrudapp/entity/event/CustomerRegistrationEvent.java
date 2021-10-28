package com.mazanenko.petproject.firstspringcrudapp.entity.event;

public class CustomerRegistrationEvent {
    private final String customerEmail;

    public CustomerRegistrationEvent(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
