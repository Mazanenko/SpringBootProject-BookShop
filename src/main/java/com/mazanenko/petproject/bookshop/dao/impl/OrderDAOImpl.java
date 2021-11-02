package com.mazanenko.petproject.bookshop.dao.impl;

import com.mazanenko.petproject.bookshop.dao.OrderDAO;
import com.mazanenko.petproject.bookshop.dao.mapper.OrderMapper;
import com.mazanenko.petproject.bookshop.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDAOImpl implements OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDAOImpl(JdbcTemplate jdbcTemplate) {
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
    public Order readOrderByCartIdAndProductId(int cartId, int productId) {
        return jdbcTemplate.query("SELECT * FROM order_table WHERE cart_id = ? and product_id = ?"
                        , new OrderMapper(), cartId, productId).stream().findAny().orElse(null);
    }

    @Override
    public List<Order> readAll() {
        return jdbcTemplate.query("SELECT * FROM order_table", new OrderMapper());
    }

    @Override
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

    @Override
    public void deleteAllByCartId(int cartId) {
        jdbcTemplate.update("DELETE FROM order_table WHERE cart_id = ?", cartId);
    }
}
