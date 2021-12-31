package com.mazanenko.petproject.bookshop.entity.event;

import com.mazanenko.petproject.bookshop.entity.Customer;

public class CustomerRegistrationEvent {
    private final Customer customer;

    public CustomerRegistrationEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
