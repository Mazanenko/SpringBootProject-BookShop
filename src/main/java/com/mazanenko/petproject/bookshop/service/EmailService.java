package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.bookshop.entity.event.OrderEvent;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void handleCustomerRegistrationEvent(CustomerRegistrationEvent event);

    void handleSubscriptionEvent(CustomerSubscriptionEvent event);

    void handleArrivalEvent(ProductArrivalEvent event);

    void handleOrderEvent(OrderEvent event);
}
