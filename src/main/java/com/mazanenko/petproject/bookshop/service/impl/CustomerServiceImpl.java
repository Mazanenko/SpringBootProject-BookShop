package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.dao.CartDAO;
import com.mazanenko.petproject.bookshop.dao.CustomerDAO;
import com.mazanenko.petproject.bookshop.dao.DeliveryAddressDAO;
import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Cart;
import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;
import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;
    private DeliveryAddressDAO addressDAO;
    private CartDAO cartDAO;
    private ApplicationEventPublisher applicationEventPublisher;

    public CustomerServiceImpl() {}

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, DeliveryAddressDAO addressDAO, CartDAO cartDAO
            , ApplicationEventPublisher applicationEventPublisher) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
        this.cartDAO = cartDAO;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createCustomer(Customer customer, DeliveryAddress address) {
        if (customer != null) {
            String cryptedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
            customer.setPassword(cryptedPassword);
            customer.setActivationCode(UUID.randomUUID().toString());
            customerDAO.create(customer);

            Customer tempCustomer = customerDAO.readByEmail(customer.getEmail());
            address.setCustomerId(tempCustomer.getId());

            addressDAO.create(address);

            Cart cart = new Cart();
            cart.setCustomerId(tempCustomer.getId());
            customer.setCart(cart);
            cartDAO.create(cart);

            if (!StringUtils.isEmpty(customer.getEmail())) {
                publishRegistrationEvent(customer.getEmail());
            }
        }
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = customerDAO.read(id);
        if (customer != null) {
            customer.setDeliveryAddress(addressDAO.read(id));
            customer.setCart(cartDAO.readByCustomerId(id));
        }
        return customer;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Customer customer = customerDAO.readByEmail(email);
        if (customer != null) {
            customer.setDeliveryAddress(addressDAO.read(customer.getId()));
            customer.setCart(cartDAO.readByCustomerId(customer.getId()));
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.readAll().stream().sorted(Comparator.comparing(Customer::getId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCustomerById(int id, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        if (!(updatedCustomer.getPassword().equals(customerDAO.read(id).getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(updatedCustomer.getPassword(), BCrypt.gensalt());
            updatedCustomer.setPassword(cryptedPassword);
        }
        updatedCustomer.setActivated(true);
        customerDAO.update(id, updatedCustomer);
        addressDAO.update(id, updatedAddress);
    }

    @Override
    public void deleteCustomerById(int id) {
        if (id > 0) {
            customerDAO.delete(id);
        }
    }

    @Override
    public void deleteCustomerByEmail(String email) {
        if (email != null) {
            customerDAO.deleteByEmail(email);
        }
    }

    @Override
    public boolean authenticatedUserIsCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"));
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) && (authentication.isAuthenticated())
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean activateUser(String code) {
        Customer customer = customerDAO.readByActivationCode(code);
        if (customer != null) {
            customer.setActivationCode(null);
            customer.setActivated(true);
            customerDAO.update(customer.getId(), customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSubscribedToArrival(Principal principal, Book book) {
        Customer customer = getCustomerByEmail(principal.getName());
        return book.getSubscribersList()!=null && book.getSubscribersList().contains(customer.getId());
    }

    private void publishRegistrationEvent(String email) {
        applicationEventPublisher.publishEvent(new CustomerRegistrationEvent(email));
    }
}
