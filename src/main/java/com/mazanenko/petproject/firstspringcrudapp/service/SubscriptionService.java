package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.Subscription;

import java.util.List;

public interface SubscriptionService {

    void subscribe(Subscription subscription);

    void subscribeByCustomerEmail(int productId, String email);

    List<Integer> listOfSubscriptions(int customerId);

    List<Integer> listOfSubscribers(int productId);

    void unsubscribe(Subscription subscription);

    void unsubscribeByCustomerEmail(int productId, String email);
}
