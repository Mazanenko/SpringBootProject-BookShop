package com.mazanenko.petproject.firstspringcrudapp.dao.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.DAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.mapper.CustomerMapper;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDAO implements DAO<Customer> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void create(Customer customer) {
        jdbcTemplate.update("INSERT INTO customer (name, surname, gender, phone, email, password) VALUES (?, ?, ?, ?, ?, ?)",
                customer.getName(), customer.getSurname(), customer.getGender(), customer.getPhone()
                , customer.getEmail(), customer.getPassword());
    }

    @Override
    public Customer read(int id) {
        return jdbcTemplate.query("SELECT * FROM customer WHERE id = ?", new CustomerMapper(), id)
                .stream().findAny().orElse(null);
    }

    public Customer readByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM customer WHERE email = ?", new CustomerMapper(), email)
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Customer> readAll() {
        return jdbcTemplate.query("SELECT * FROM customer", new CustomerMapper());
    }

    @Override
    public void update(int id, Customer customer) {
        jdbcTemplate.update("UPDATE customer SET name = ?, surname = ?, gender = ?, phone = ?, email = ?, password = ? WHERE id = ?",
                customer.getName(), customer.getSurname(), customer.getGender(), customer.getPhone(), customer.getEmail()
                , customer.getPassword(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM customer WHERE id = ?", id);
    }

    public void delete(String email) {
        jdbcTemplate.update("DELETE FROM customer WHERE email = ?", email);
    }
}
