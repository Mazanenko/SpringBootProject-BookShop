package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;

import java.util.List;

public interface CustomerService {
    void createCustomer(Customer customer);

    Customer getCustomerById(int id);

    List<Customer> getAllCustomers();

    void updateCustomerById(int id, Customer updatedCustomer);

    void deleteCustomerById(int id);
}
