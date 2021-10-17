package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;

import java.sql.SQLException;

public interface CartService {

    Cart getCartById(int cartId);

    Cart getCartByCustomerEmail(String email);

    Cart getCartByCustomerId(int id);

    void addOrderToCartById(Order order) throws SQLException;

    void updateOrderInCartById(int orderId, Order order);

    void deleteOrderFromCartById(int orderId);

    void deleteAllOrdersFromCart(int cartId);
}
