package com.mazanenko.petproject.firstspringcrudapp.dao.mapper;

import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Order order = new Order();

        order.setCartId(resultSet.getInt("cart_id"));
        order.setProductId(resultSet.getInt("product_id"));
        order.setQuantity(resultSet.getInt("quantity"));

        return order;
    }
}
