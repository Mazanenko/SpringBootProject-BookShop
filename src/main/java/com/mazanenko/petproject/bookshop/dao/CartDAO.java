package com.mazanenko.petproject.bookshop.dao;

import com.mazanenko.petproject.bookshop.entity.Cart;

import java.util.List;

public interface CartDAO {

    void create(Cart cart);

    Cart read(int id);

    Cart readByCustomerId(int id);

    List<Cart> readAll();

    void update(int id, Cart cart);

    void delete(int id);
}
