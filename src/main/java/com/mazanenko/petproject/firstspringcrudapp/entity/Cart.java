package com.mazanenko.petproject.firstspringcrudapp.entity;

public class Cart {
    private int id;
    private int customerId;

    private Order order;

    public Cart() {}

    public Cart(int id, int customerId, Order order) {
        this.id = id;
        this.customerId = customerId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
