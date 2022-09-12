package com.mazanenko.petproject.bookshop.service;

import org.springframework.lang.NonNull;

public interface SubscriptionService {

    void subscribeByCustomerEmail(@NonNull Long productId, @NonNull  String email);

    void unsubscribeByCustomerEmail(@NonNull  Long productId, @NonNull  String email);
}
