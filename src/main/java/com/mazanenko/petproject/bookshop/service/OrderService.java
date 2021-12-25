package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(Order order);

    Order readOrder(Long orderId);

    Order readOrderByCartIdAndProductId(Long cartId, Long productId);

    List<Order> readAllOrders();

    List<Order> readALLOrdersByCartId(Long cartId);

    void updateOrder(Long orderId, Order updatedOrder);

    void incrementOrderQuantity(Long orderId);

    void decrementOrderQuantity(Long orderId);

    void deleteOrder(Long orderId);

    void deleteAllOrdersByCartId(Long cartId);
}
