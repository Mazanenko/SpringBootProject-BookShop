package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.OrderMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDAO implements DAO<Order> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void create(Order order) {
        jdbcTemplate.update("INSERT INTO order_table (cart_id, product_id, quantity) VALUES (?, ?, ?)"
        , order.getCartId(), order.getProductId(), order.getQuantity());
    }

    @Override
    public Order read(int id) {

        return jdbcTemplate.query("SELECT * FROM order_table WHERE id = ?", new OrderMapper(), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Order> readAll() {
        return jdbcTemplate.query("SELECT * FROM order_table", new OrderMapper());
    }

    public List<Order> readALLByCartId(int cartId) {
        return jdbcTemplate.query("SELECT * FROM order_table WHERE cart_id = ?", new OrderMapper(), cartId);
    }

    @Override
    public void update(int id, Order order) {
        jdbcTemplate.update("UPDATE order_table SET cart_id = ?, product_id = ?, quantity = ? WHERE id = ?"
        , order.getCartId(), order.getProductId(), order.getQuantity(), order.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM order_table WHERE id = ?", id);
    }

    public void deleteAll(int cartId) {
        jdbcTemplate.update("DELETE FROM order_table WHERE cart_id = ?", cartId);
    }
}
