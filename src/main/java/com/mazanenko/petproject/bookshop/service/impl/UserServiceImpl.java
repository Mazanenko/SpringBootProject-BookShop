package com.mazanenko.petproject.bookshop.service.impl;


import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Person;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
public class UserServiceImpl implements com.mazanenko.petproject.bookshop.service.UserService {
    private CustomerService customerService;
    private ManagerService managerService;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(CustomerService customerService, ManagerService managerService) {
        this.customerService = customerService;
        this.managerService = managerService;
    }

    @Override
    public Person findByEmail(String email) throws UsernameNotFoundException {
        Customer customer = customerService.getCustomerByEmail(email);
        Person manager = managerService.getManagerByEmail(email);

        if (customer != null) {
            if (customer.isActivated()) {
                return customer;
            } else throw new UsernameNotFoundException(String.format("Email %s not activated yet", email));

        } else {
            if (manager != null) {
                return manager;
            } else throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = findByEmail(email);
        return new User(person.getEmail(), person.getPassword(), rolesToAuthorities(person.getRole()));
    }


    private Collection<? extends GrantedAuthority> rolesToAuthorities(String role) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        list.add(grantedAuthority);
        return list;
    }
}
