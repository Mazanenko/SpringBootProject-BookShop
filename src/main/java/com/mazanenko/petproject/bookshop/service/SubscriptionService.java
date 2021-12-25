package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Subscription;

public interface SubscriptionService {

    void subscribe(Subscription subscription);

    void subscribeByCustomerEmail(Long productId, String email);

    void unsubscribe(Subscription subscription);

    void unsubscribeByCustomerEmail(Long productId, String email);
}
