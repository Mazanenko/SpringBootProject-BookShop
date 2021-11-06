package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Person;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Person findByEmail(String email);


}
