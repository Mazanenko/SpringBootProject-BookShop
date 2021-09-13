package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;

import java.util.List;


public interface CustomerDAO {
    void create(Customer customer);

    Customer read(int id);

    List<Customer> readAll();

    void update(int id, Customer customer);

    void delete(int id);
}
