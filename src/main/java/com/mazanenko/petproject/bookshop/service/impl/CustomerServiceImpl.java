package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.annotation.LogException;
import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.repository.CustomerRepository;
import com.mazanenko.petproject.bookshop.service.CartService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

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
    @LogException
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
        publishRegistrationEvent(customer);

        LOGGER.info("Customer {} {} with email {} successfully created",
                customer.getName(), customer.getSurname(), customer.getEmail());
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
    @Transactional
    public void updateCustomerById(Long customerId, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        if (customerId <= 0 || updatedCustomer == null || updatedAddress == null) {
            return;
        }

        Customer originalCustomer = customerRepo.findById(customerId).orElse(null);
        if (originalCustomer == null) {
            return;
        }
        updatedCustomer.setId(customerId);
        updatedCustomer.setActivated(true);
        updatedCustomer.setCart(originalCustomer.getCart());
        updatedCustomer.setSubscriptions(originalCustomer.getSubscriptions());
        updatedAddress.setId(customerId);

        updatePassword(originalCustomer, updatedCustomer);
        updateAddress(originalCustomer, updatedCustomer, updatedAddress);
        customerRepo.save(updatedCustomer);

        if (!originalCustomer.equals(updatedCustomer)) {
            LOGGER.info("The customer's profile {} {} with ID {} and email {} was updated. Now it is {}",
                    originalCustomer.getName(), originalCustomer.getSurname(), originalCustomer.getId(),
                    originalCustomer.getEmail(), updatedCustomer);
        }
    }


    @Override
    @Transactional
    public void deleteCustomerById(Long customerId) {
        if (customerId <= 0) {
            return;
        }

        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            return;
        }

        deleteAllOrdersFromCustomerCart(customer);
        customerRepo.deleteById(customerId);

        LOGGER.info("The customer's profile {} {} with ID {} and email {} was deleted", customer.getName(),
                customer.getSurname(), customer.getId(), customer.getEmail());
    }

    @Override
    @Transactional
    public void deleteCustomerByEmail(String email) {
        if (email == null) {
            return;
        }

        Customer customer = getCustomerByEmail(email);
        if (customer == null) {
            return;
        }

        deleteAllOrdersFromCustomerCart(customer);
        customerRepo.deleteByEmail(email);

        LOGGER.info("The customer's profile {} {} with ID {} and email {} was deleted", customer.getName(),
                customer.getSurname(), customer.getId(), customer.getEmail());
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
            LOGGER.info("The customer's profile {} {} with ID {} and email {} was activated", customer.getName(),
                    customer.getSurname(), customer.getId(), customer.getEmail());
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

        return customer.getSubscriptions() != null
                && customer.getSubscriptions().stream().map(Subscription::getBook)
                .anyMatch(book1 -> book1.equals(book));
    }


    private void publishRegistrationEvent(Customer customer) {
        if (customer == null) {
            return;
        }
        applicationEventPublisher.publishEvent(new CustomerRegistrationEvent(customer));
    }

    private void deleteAllOrdersFromCustomerCart(Customer customer) {
        if (customer == null) {
            return;
        }
        cartService.deleteAllOrdersFromCart(customer.getCart());
    }

    private void updatePassword(Customer originalCustomer, Customer updatedCustomer) {
        if (originalCustomer == null || updatedCustomer == null) {
            return;
        }
        if (updatedCustomer.getPassword().equals(originalCustomer.getPassword())) {
            return;
        }

        String cryptedPassword = BCrypt.hashpw(updatedCustomer.getPassword(), BCrypt.gensalt());
        updatedCustomer.setPassword(cryptedPassword);

        LOGGER.info("Password for customer {} {} with ID {} and email {} was updated",
                originalCustomer.getName(), originalCustomer.getSurname(), originalCustomer.getId(),
                originalCustomer.getEmail());
    }

    private void updateAddress(Customer originalCustomer, Customer updatedCustomer, DeliveryAddress updatedAddress) {
        if (originalCustomer == null || updatedCustomer == null || updatedAddress == null) {
            return;
        }

        DeliveryAddress originalAddress = originalCustomer.getDeliveryAddress();
        if (!originalAddress.equals(updatedAddress)) {

            updatedCustomer.setDeliveryAddress(updatedAddress);

            LOGGER.info("The delivery address for customer {} {} with ID {} and email {} was updated. Now it is {}",
                    originalCustomer.getName(), originalCustomer.getSurname(), originalCustomer.getId(),
                    originalCustomer.getEmail(), updatedAddress);

        } else updatedCustomer.setDeliveryAddress(originalAddress);
    }

}
