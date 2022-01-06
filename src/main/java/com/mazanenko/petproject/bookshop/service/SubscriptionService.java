package com.mazanenko.petproject.bookshop.service;

public interface SubscriptionService {

    void subscribeByCustomerEmail(Long productId, String email);

    void unsubscribeByCustomerEmail(Long productId, String email);
}
