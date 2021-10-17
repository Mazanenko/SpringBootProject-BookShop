package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(Order order);

    Order readOrder(int id);

    List<Order> readAllOrders();

    List<Order> readALLOrdersByCartId(int cartId);

    void updateOrder(int id, Order order);

    void deleteOrder(int id);

    void deleteAllOrdersByCartId(int cartId);
}
