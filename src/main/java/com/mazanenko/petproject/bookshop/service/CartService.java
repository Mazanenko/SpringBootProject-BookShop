package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Order;

import java.sql.SQLException;

public interface CartService {

    Cart getCartById(Long cartId);

    Cart getCartByCustomerEmail(String email);

    Cart getCartByCustomerId(Long customerId);

    void addToCart(Order order) throws SQLException;

    void addToCartByCustomerId(Long customerId, Long bookId) throws SQLException;

    void addToCartByCustomerEmail(String email, Long bookId) throws SQLException;

    void incrementProduct(Long productId, Cart cart) throws SQLException;

    void decrementProduct(Long productId, Cart cart);

    void deleteOrderFromCart(Long productId, Cart cart);

    void deleteAllOrdersFromCart(Long cartId);

    void makeAnOrderByCustomerEmail(String email);
}
