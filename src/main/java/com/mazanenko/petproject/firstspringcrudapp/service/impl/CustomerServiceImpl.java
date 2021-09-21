package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.impl.CustomerDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.impl.DeliveryAddressDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final DeliveryAddressDAO addressDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, DeliveryAddressDAO addressDAO) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
    }

    @Override
    public void createCustomer(Customer customer, DeliveryAddress address) {
        Customer tempCustomer;
        customerDAO.create(customer);

        tempCustomer = customerDAO.readByEmail(customer.getEmail());
        address.setCustomerId(tempCustomer.getId());

        addressDAO.create(address);

    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = customerDAO.read(id);
        customer.setDeliveryAddress(addressDAO.read(id));
        return customer;
    }

    // no need?
    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    @Override
    public void updateCustomerById(int id, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        customerDAO.update(id, updatedCustomer);
        addressDAO.update(id, updatedAddress);
    }

    @Override
    public void deleteCustomerById(int id) {
        customerDAO.delete(id);
    }
}
