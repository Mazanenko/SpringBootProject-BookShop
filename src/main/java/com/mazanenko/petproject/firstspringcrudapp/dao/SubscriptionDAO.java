package com.mazanenko.petproject.firstspringcrudapp.dao;

import com.mazanenko.petproject.firstspringcrudapp.entity.Subscription;

import java.util.List;

public interface SubscriptionDAO {

    void create(Subscription subscription);

    Subscription read(Subscription subscription);

    List<Subscription> readAllByCustomerId(int customerId);

    List<Subscription> readAllByProductId(int productId);

    void delete(Subscription subscription);
}
