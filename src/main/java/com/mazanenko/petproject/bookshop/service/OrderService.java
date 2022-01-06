package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(Order order);

    Order readOrder(Long orderId);

    Order readOrderByCartIdAndProductId(Long cartId, Long productId);

    List<Order> readAllOrders();

    List<Order> readALLOrdersByCartId(Long cartId);

    void updateOrder(Order updatedOrder);

    void incrementOrderQuantity(Order order);

    void decrementOrderQuantity(Order order);

    void deleteOrder(Order order);

    void deleteAllOrdersByCartId(Long cartId);
}
