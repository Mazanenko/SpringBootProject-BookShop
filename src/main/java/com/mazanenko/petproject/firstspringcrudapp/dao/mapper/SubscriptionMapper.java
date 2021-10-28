package com.mazanenko.petproject.firstspringcrudapp.dao.mapper;

import com.mazanenko.petproject.firstspringcrudapp.entity.Subscription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionMapper implements RowMapper<Subscription> {

    @Override
    public Subscription mapRow(ResultSet resultSet, int i) throws SQLException {
        Subscription subscription = new Subscription();

        subscription.setProductId(resultSet.getInt("product_id"));
        subscription.setCustomerId(resultSet.getInt("customer_id"));

        return subscription;
    }
}
