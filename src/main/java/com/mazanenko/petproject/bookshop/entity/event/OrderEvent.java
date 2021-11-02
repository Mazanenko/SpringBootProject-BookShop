package com.mazanenko.petproject.bookshop.entity.event;

import com.mazanenko.petproject.bookshop.entity.Cart;

public class OrderEvent {
    private final Cart cart;

    public OrderEvent(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }
}
