package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;

public interface CartService {

    Cart getCartById(int cartId);

    void addOrderToCartById(Order order);

    void updateOrderInCartById(int orderId, Order order);

    void deleteOrderFromCartById(int orderId);

    void deleteAllOrdersFromCart(int cartId);
}
