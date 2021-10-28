package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.SubscriptionDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.Subscription;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import com.mazanenko.petproject.firstspringcrudapp.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionDAO subscriptionDAO;
    private final CustomerService customerService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionDAO subscriptionDAO, CustomerService customerService,
                                   ApplicationEventPublisher applicationEventPublisher) {
        this.subscriptionDAO = subscriptionDAO;
        this.customerService = customerService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void subscribe(Subscription subscription) {
        if (subscriptionDAO.read(subscription) == null) {
            subscriptionDAO.create(subscription);
        }
    }

    @Override
    public void subscribeByCustomerEmail(int productId, String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            subscribe(new Subscription(productId, customer.getId()));
            publishSubscriptionEvent("subscribed", email, productId);
        }
    }

    @Override
    public List<Integer> listOfSubscriptions(int customerId) {
        List<Subscription> subscriptions = subscriptionDAO.readAllByCustomerId(customerId);
        if (!subscriptions.isEmpty()) {
            return subscriptions.stream().map(Subscription::getProductId).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Integer> listOfSubscribers(int productId) {
        List<Subscription> subscriptions = subscriptionDAO.readAllByProductId(productId);
        if (!subscriptions.isEmpty()) {
            return subscriptions.stream().map(Subscription::getCustomerId).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void unsubscribe(Subscription subscription) {
        if (subscriptionDAO.read(subscription) != null) {
            subscriptionDAO.delete(subscription);
        }
    }

    @Override
    public void unsubscribeByCustomerEmail(int productId, String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            unsubscribe(new Subscription(productId, customer.getId()));
            publishSubscriptionEvent("unsubscribed", email, productId);
        }
    }

    private void publishSubscriptionEvent(String name, String customerEmail, int productId) {
        applicationEventPublisher.publishEvent(new CustomerSubscriptionEvent(name, customerEmail, productId));
    }
}
