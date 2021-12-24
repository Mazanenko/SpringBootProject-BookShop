package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;

import java.security.Principal;
import java.util.List;

public interface CustomerService {
    void createCustomer(Customer customer, DeliveryAddress address);

    Customer getCustomerById(Long customerId);

    Customer getCustomerByEmail(String email);

    List<Customer> getAllCustomers();

    void updateCustomerById(Long customerId, Customer updatedCustomer, DeliveryAddress address);

    void deleteCustomerById(Long customerId);

    void deleteCustomerByEmail(String email);

    boolean authenticatedUserIsCustomer();

    boolean isAuthenticated();

    boolean activateUser(String code);

    boolean isSubscribedToArrival(Principal principal, Book book);
}
