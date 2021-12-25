package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.repository.CustomerRepository;
import com.mazanenko.petproject.bookshop.service.CartService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepo;
    private CartService cartService;
    private ApplicationEventPublisher applicationEventPublisher;

    public CustomerServiceImpl() {
    }

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepo, CartService cartService,
                               ApplicationEventPublisher applicationEventPublisher) {
        this.customerRepo = customerRepo;
        this.cartService = cartService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createCustomer(Customer customer, DeliveryAddress address) {
        if (customer == null || address == null) {
            return;
        }

        String cryptedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
        customer.setPassword(cryptedPassword);
        customer.setActivationCode(UUID.randomUUID().toString());

        address.setCustomer(customer);
        customer.setDeliveryAddress(address);

        Cart cart = new Cart();
        cart.setCustomer(customer);
        customer.setCart(cart);

        customerRepo.save(customer);
        publishRegistrationEvent(customer.getEmail());
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        if (customerId <= 0) {
            return null;
        }

        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }

        customer.setRole("ROLE_CUSTOMER");
        return customer;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        if (email == null) {
            return null;
        }

        Customer customer = customerRepo.findByEmail(email).orElse(null);
        if (customer == null) {
            return null;
        }

        customer.setRole("ROLE_CUSTOMER");
        return customer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public void updateCustomerById(Long customerId, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        if (customerId <= 0 || updatedCustomer == null || updatedAddress == null) {
            return;
        }

        Customer tempCustomer = customerRepo.findById(customerId).orElse(null);
        if (tempCustomer == null) {
            return;
        }

        if (!(updatedCustomer.getPassword().equals(tempCustomer.getPassword()))) {
            String cryptedPassword = BCrypt.hashpw(updatedCustomer.getPassword(), BCrypt.gensalt());
            updatedCustomer.setPassword(cryptedPassword);
        }

        updatedCustomer.setId(customerId);
        updatedCustomer.setActivated(true);

        updatedAddress.setId(customerId);
        updatedCustomer.setDeliveryAddress(updatedAddress);

        updatedCustomer.setCart(tempCustomer.getCart());
        updatedCustomer.setSubscriptions(tempCustomer.getSubscriptions());

        customerRepo.save(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomerById(Long customerId) {
        if (customerId <= 0) {
            return;
        }

        Customer customer = getCustomerById(customerId);
        if (customer != null) {
            cartService.deleteAllOrdersFromCart(customer.getCart().getId());
            customerRepo.deleteById(customerId);
        }
    }

    @Override
    @Transactional
    public void deleteCustomerByEmail(String email) {
        if (email == null) {
            return;
        }

        Customer customer = getCustomerByEmail(email);
        if (customer != null) {
            cartService.deleteAllOrdersFromCart(customer.getCart().getId());
            customerRepo.deleteByEmail(email);
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
        if (code == null) {
            return false;
        }

        Customer customer = customerRepo.findByActivationCode(code).orElse(null);
        if (customer != null) {
            customer.setActivationCode(null);
            customer.setActivated(true);
            customerRepo.save(customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSubscribedToArrival(Principal principal, Book book) {
        if (principal == null || book == null) {
            return false;
        }

        Customer customer = getCustomerByEmail(principal.getName());
        if (customer == null) {
            return false;
        }
        return book.getSubscribersList() != null && book.getSubscribersList().stream()
                .map(Subscription::getCustomer).anyMatch(customer1 -> customer1.equals(customer));
    }

    private void publishRegistrationEvent(String email) {
        if (email == null) {
            return;
        }
        applicationEventPublisher.publishEvent(new CustomerRegistrationEvent(email));
    }
}
