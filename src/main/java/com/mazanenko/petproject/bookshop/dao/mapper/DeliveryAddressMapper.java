package com.mazanenko.petproject.bookshop.dao.mapper;

import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryAddressMapper implements RowMapper<DeliveryAddress> {
    @Override
    public DeliveryAddress mapRow(ResultSet resultSet, int i) throws SQLException {
        DeliveryAddress deliveryAddress = new DeliveryAddress();

        deliveryAddress.setCustomerId(resultSet.getInt("customer_id"));
        deliveryAddress.setCountry(resultSet.getString("country"));
        deliveryAddress.setCity(resultSet.getString("city"));
        deliveryAddress.setStreet(resultSet.getString("street"));
        deliveryAddress.setHouseNumber(resultSet.getInt("house_number"));
        deliveryAddress.setNote(resultSet.getString("note"));

        return deliveryAddress;
    }
}
