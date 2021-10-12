package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.impl.CartDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.impl.CustomerDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.impl.DeliveryAddressDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import com.mazanenko.petproject.firstspringcrudapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final DeliveryAddressDAO addressDAO;
    private final CartDAO cartDAO;
    private final EmailService emailService;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, DeliveryAddressDAO addressDAO, CartDAO cartDAO
            , EmailService emailService) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
        this.cartDAO = cartDAO;
        this.emailService = emailService;
    }

    @Override
    public void createCustomer(Customer customer, DeliveryAddress address) {
        Customer tempCustomer;

        String cryptedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
        customer.setPassword(cryptedPassword);
        customerDAO.create(customer);

        tempCustomer = customerDAO.readByEmail(customer.getEmail());
        address.setCustomerId(tempCustomer.getId());

        addressDAO.create(address);

        Cart cart = new Cart();
        cart.setCustomerId(tempCustomer.getId());
        customer.setCart(cart);
        cartDAO.create(cart);

        if (!StringUtils.isEmpty(customer.getEmail())) {
            String message = String.format("Hello, %s! \n" + "Welcome to Booksland! You already complete " +
                    "registration and now You can order as many books as you want.", customer.getName());
            emailService.sendSimpleMessage(customer.getEmail(), "Registration on booksland", message);
        }
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = customerDAO.read(id);
        if (customer != null) {
            customer.setDeliveryAddress(addressDAO.read(id));
        }
        return customer;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Customer customer = customerDAO.readByEmail(email);
        if (customer != null) {
            customer.setDeliveryAddress(addressDAO.read(customer.getId()));
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    @Override
    public void updateCustomerById(int id, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        if (!(updatedCustomer.getPassword().equals(customerDAO.read(id).getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(updatedCustomer.getPassword(), BCrypt.gensalt());
            updatedCustomer.setPassword(cryptedPassword);
        }
        customerDAO.update(id, updatedCustomer);
        addressDAO.update(id, updatedAddress);
    }

    @Override
    public void deleteCustomerById(int id) {
        customerDAO.delete(id);
    }

    @Override
    public void deleteCustomerByEmail(String email) {
        customerDAO.delete(email);
    }
}
