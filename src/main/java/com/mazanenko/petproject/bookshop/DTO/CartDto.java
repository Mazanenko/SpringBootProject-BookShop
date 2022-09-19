package com.mazanenko.petproject.bookshop.DTO;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Order;
import lombok.Data;

import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private Set<Order> orders;
    private Customer customer;
}
