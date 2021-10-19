package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.CartDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.CustomerDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.DeliveryAddressDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import com.mazanenko.petproject.firstspringcrudapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            String message = String.format("Hello, %s! \n" + "Welcome to Booksland! Please, visit next link: " +
                            "http://localhost:8080/customer/activate/%s to activate your account and complete registration."
                    , customer.getName(), customer.getActivationCode());
            emailService.sendSimpleMessage(customer.getEmail(), "Registration on booksland", message);
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
        customerDAO.update(id, updatedCustomer);
        addressDAO.update(id, updatedAddress);
    }

    @Override
    public void deleteCustomerById(int id) {
        customerDAO.delete(id);
    }

    @Override
    public void deleteCustomerByEmail(String email) {
        customerDAO.deleteByEmail(email);
    }

    @Override
    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("error while login" + e.getMessage());
            e.printStackTrace();
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
}
