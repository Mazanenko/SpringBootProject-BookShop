package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;

import java.util.List;

public interface CartDAO {

    void create(Cart cart);

    Cart read(int id);

    List<Cart> readAll();

    void update(int id, Cart cart);

    void delete(int id);
}
