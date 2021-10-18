package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;

import java.sql.SQLException;

public interface CartService {

    Cart getCartById(int cartId);

    Cart getCartByCustomerEmail(String email);

    Cart getCartByCustomerId(int id);

    void addOrderToCart(Order order) throws SQLException;

    void incrementProduct(int productId, Cart cart) throws SQLException;

    void decrementProduct(int productId, Cart cart);

    void updateOrderInCartById(int orderId, Order order);

    void deleteOrderFromCart(int productId, Cart cart);

    void deleteAllOrdersFromCart(int cartId);
}
