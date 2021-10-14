package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomerService {
    void createCustomer(Customer customer, DeliveryAddress address);

    Customer getCustomerById(int id);

    Customer getCustomerByEmail(String email);

    List<Customer> getAllCustomers();

    void updateCustomerById(int id, Customer updatedCustomer, DeliveryAddress address);

    void deleteCustomerById(int id);

    void deleteCustomerByEmail(String email);

    void authWithHttpServletRequest(HttpServletRequest request, String username, String password);

    boolean authenticatedUserIsCustomer();

    boolean isAuthenticated();

    boolean activateUser(String code);
}
