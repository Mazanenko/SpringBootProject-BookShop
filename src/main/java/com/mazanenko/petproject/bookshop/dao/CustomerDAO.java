package com.mazanenko.petproject.bookshop.dao;

import com.mazanenko.petproject.bookshop.entity.Customer;

import java.util.List;

public interface CustomerDAO {

    void create(Customer customer);

    Customer read(int id);

    Customer readByEmail(String email);

    Customer readByActivationCode(String code);

    List<Customer> readAll();

    void update(int id, Customer customer);

    void delete(int id);

    void deleteByEmail(String email);
}
