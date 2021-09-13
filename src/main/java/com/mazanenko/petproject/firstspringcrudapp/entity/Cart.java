package com.mazanenko.petproject.firstspringcrudapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id;
    private int customerId;

    private List<Order> orderList = new ArrayList<>();

    public Cart() {}

    public Cart(int id, int customerId, List<Order> orderList) {
        this.id = id;
        this.customerId = customerId;
        this.orderList = orderList;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + getId() +
                ", customerId=" + getCustomerId() +
                '}';
    }
}
