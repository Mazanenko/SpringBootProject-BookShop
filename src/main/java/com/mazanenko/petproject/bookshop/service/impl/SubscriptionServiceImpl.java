package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Subscription;
import com.mazanenko.petproject.bookshop.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.bookshop.repository.SubscriptionRepository;
import com.mazanenko.petproject.bookshop.service.BookService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import com.mazanenko.petproject.bookshop.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository subscriptionRepo;
    private CustomerService customerService;
    private BookService bookService;
    private ApplicationEventPublisher applicationEventPublisher;
    private final static Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    public SubscriptionServiceImpl() {
    }

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepo, CustomerService customerService,
                                   BookService bookService, ApplicationEventPublisher applicationEventPublisher) {
        this.subscriptionRepo = subscriptionRepo;
        this.customerService = customerService;
        this.bookService = bookService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void subscribeByCustomerEmail(Long productId, String email) {
        if (productId <= 0 || email == null) {
            return;
        }

        Customer customer = customerService.getCustomerByEmail(email);
        Book book = bookService.getBookById(productId);
        if (customer == null || book == null) {
            return;
        }

        subscribe(new Subscription(book, customer));
        publishSubscriptionEvent("subscribed", email, productId);

        LOGGER.info("The customer {} {} with ID {} and email {} subscribed to new arrives of book {} by {} with ID {}",
                customer.getName(), customer.getSurname(), customer.getId(), customer.getEmail(),
                book.getName(), book.getAuthor(), book.getId());
    }

    @Override
    @Transactional
    public void unsubscribeByCustomerEmail(Long productId, String email) {
        if (productId <= 0 || email == null) {
            return;
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return;
        }

        Subscription subscription = getSubscriptionByBookId(productId, customer);
        if (subscription == null) {
            return;
        }

        unsubscribe(subscription);
        customer.getSubscriptions().removeIf(subscript -> subscript.getBook().getId().equals(productId));
        publishSubscriptionEvent("unsubscribed", email, productId);

        LOGGER.info("The customer {} {} with ID {} and email {} unsubscribed to new arrives of book with ID {}",
                customer.getName(), customer.getSurname(), customer.getId(), customer.getEmail(),
                productId);
    }


    private void subscribe(Subscription subscription) {
        if (subscription == null) {
            return;
        }
        subscriptionRepo.save(subscription);
    }

    private void unsubscribe(Subscription subscription) {
        if (subscription == null) {
            return;
        }
        subscriptionRepo.delete(subscription);
    }

    private void publishSubscriptionEvent(String name, String customerEmail, Long productId) {
        if (name == null || customerEmail == null || productId <= 0) {
            return;
        }
        if (name.toLowerCase(Locale.ROOT).equals("subscribed") ||
                name.toLowerCase(Locale.ROOT).equals("unsubscribed")) {
            applicationEventPublisher.publishEvent(new CustomerSubscriptionEvent(name, customerEmail, productId));
        }
    }

    private Subscription getSubscriptionByBookId(Long bookId, Customer customer) {
        if (bookId == null || customer == null) {
            return null;
        }
        return customer.getSubscriptions().stream()
                .filter(subscription1 -> subscription1.getBook().getId().equals(bookId))
                .findAny().orElse(null);
    }
}
