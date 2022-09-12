package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Order;
import org.springframework.lang.NonNull;

import java.util.List;

public interface OrderService {

    void createOrder(@NonNull Order order);

    Order readOrder(@NonNull Long orderId);

    Order readOrderByCartIdAndProductId(@NonNull Long cartId, @NonNull Long productId);

    List<Order> readAllOrders();

    List<Order> readALLOrdersByCartId(@NonNull Long cartId);

    void updateOrder(@NonNull Order updatedOrder);

    void incrementOrderQuantity(@NonNull Order order);

    void decrementOrderQuantity(@NonNull Order order);

    void deleteOrder(@NonNull Order order);

    void deleteAllOrdersByCartId(@NonNull Long cartId);
}
