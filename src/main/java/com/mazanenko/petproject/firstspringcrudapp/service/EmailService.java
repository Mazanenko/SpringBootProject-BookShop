package com.mazanenko.petproject.firstspringcrudapp.service;

import com.mazanenko.petproject.firstspringcrudapp.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.OrderEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.ProductArrivalEvent;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void handleCustomerRegistrationEvent(CustomerRegistrationEvent event);

    void handleSubscriptionEvent(CustomerSubscriptionEvent event);

    void handleArrivalEvent(ProductArrivalEvent event);

    void handleOrderEvent(OrderEvent event);
}
