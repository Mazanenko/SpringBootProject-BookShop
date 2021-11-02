package com.mazanenko.petproject.bookshop.dao.mapper;

import com.mazanenko.petproject.bookshop.entity.Cart;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartMapper implements RowMapper<Cart> {

    @Override
    public Cart mapRow(ResultSet resultSet, int i) throws SQLException {
        Cart cart = new Cart();

        cart.setId(resultSet.getInt("id"));
        cart.setCustomerId(resultSet.getInt("customer_id"));

        return cart;
    }
}
