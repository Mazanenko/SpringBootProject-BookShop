package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Order;

import java.util.List;

public interface OrderDAO {

    void create(Order order);

    Order read(int id);

    Order readOrderByCartIdAndProductId(int cartId, int productId);

    List<Order> readAll();

    List<Order> readALLByCartId(int cartId);

    void update(int id, Order order);

    void delete(int id);

    void deleteAllByCartId(int cartId);
}
