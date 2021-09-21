package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.DeliveryAddressMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryAddressDAO implements DAO<DeliveryAddress> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DeliveryAddressDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(DeliveryAddress deliveryAddress) {
        jdbcTemplate.update("INSERT INTO delivery_address (customer_id, country, city, street, house_number, note) " +
                "VALUES (?, ?, ?, ?, ?, ?)", deliveryAddress.getCustomerId(), deliveryAddress.getCountry(), deliveryAddress.getCity(),
                deliveryAddress.getStreet(), deliveryAddress.getHouseNumber(), deliveryAddress.getNote());
    }

    @Override
    public DeliveryAddress read(int id) {
        return jdbcTemplate.query("SELECT * FROM delivery_address WHERE customer_id = ?", new DeliveryAddressMapper(), id)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<DeliveryAddress> readAll() {
        return jdbcTemplate.query("SELECT * FROM delivery_address", new DeliveryAddressMapper());
    }

    @Override
    public void update(int id, DeliveryAddress deliveryAddress) {
        jdbcTemplate.update("UPDATE delivery_address SET country = ?, city = ?, street = ?, " +
                "house_number = ?, note = ? WHERE customer_id = ?", deliveryAddress.getCountry(), deliveryAddress.getCity(),
                deliveryAddress.getStreet(), deliveryAddress.getHouseNumber(), deliveryAddress.getNote(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM delivery_address WHERE customer_id = ?", id);
    }
}
