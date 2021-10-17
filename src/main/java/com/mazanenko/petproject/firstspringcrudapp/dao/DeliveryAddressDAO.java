package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressDAO {

    void create(DeliveryAddress deliveryAddress);

    DeliveryAddress read(int id);

    List<DeliveryAddress> readAll();

    void update(int id, DeliveryAddress deliveryAddress);


    void delete(int id);
}
