package com.mazanenko.petproject.bookshop.dao;

import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressDAO {

    void create(DeliveryAddress deliveryAddress);

    DeliveryAddress read(int id);

    List<DeliveryAddress> readAll();

    void update(int id, DeliveryAddress deliveryAddress);


    void delete(int id);
}
