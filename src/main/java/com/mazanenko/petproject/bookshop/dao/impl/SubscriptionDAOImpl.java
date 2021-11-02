package com.mazanenko.petproject.bookshop.dao.impl;

import com.mazanenko.petproject.bookshop.dao.SubscriptionDAO;
import com.mazanenko.petproject.bookshop.dao.mapper.SubscriptionMapper;
import com.mazanenko.petproject.bookshop.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionDAOImpl implements SubscriptionDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubscriptionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Subscription subscription) {
        jdbcTemplate.update("INSERT INTO subscription (product_id, customer_id) VALUES (?, ?)",
                subscription.getProductId(), subscription.getCustomerId());
    }

    @Override
    public Subscription read(Subscription subscription) {
        return jdbcTemplate.query("SELECT * FROM subscription WHERE customer_id = ? AND product_id = ?",
                new SubscriptionMapper(), subscription.getCustomerId(), subscription.getProductId()).stream()
                .findAny().orElse(null);
    }

    @Override
    public List<Subscription> readAllByCustomerId(int customerId) {
        return jdbcTemplate.query("SELECT * FROM subscription WHERE customer_id = ?",
                new SubscriptionMapper(), customerId);
    }

    @Override
    public List<Subscription> readAllByProductId(int productId) {
        return jdbcTemplate.query("SELECT * FROM subscription WHERE product_id = ?",
                new SubscriptionMapper(), productId);
    }

    @Override
    public void delete(Subscription subscription) {
        jdbcTemplate.update("DELETE FROM subscription WHERE product_id = ? AND customer_id = ?",
                subscription.getProductId(), subscription.getCustomerId());
    }
}
