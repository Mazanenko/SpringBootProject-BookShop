package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(Order order);

    Order readOrder(int id);

    Order readOrderByCartIdAndProductId(int cartId, int productId);

    List<Order> readAllOrders();

    List<Order> readALLOrdersByCartId(int cartId);

    void updateOrder(int id, Order order);

    void incrementOrderQuantity(int orderId);

    void decrementOrderQuantity(int orderId);

    void deleteOrder(int id);

    void deleteAllOrdersByCartId(int cartId);
}
