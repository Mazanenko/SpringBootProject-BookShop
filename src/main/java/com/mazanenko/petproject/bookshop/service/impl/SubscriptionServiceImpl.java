package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.Product;
import com.mazanenko.petproject.bookshop.entity.Subscription;
import com.mazanenko.petproject.bookshop.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.bookshop.repository.CustomerRepository;
import com.mazanenko.petproject.bookshop.repository.ProductRepository;
import com.mazanenko.petproject.bookshop.repository.SubscriptionRepository;
import com.mazanenko.petproject.bookshop.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void subscribeByCustomerEmail(@NonNull Long productId, @NonNull String email) {
        if (productId < 1L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id can't be less then 1");
        }
        Customer customer = findCustomerByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Product with id %s doesn't exist", productId)));

        subscriptionRepo.save(new Subscription(product, customer));
        publishSubscriptionEvent("subscribed", email, productId);

        log.info("The customer {} {} with ID {} and email {} subscribed to new arrives of product {} with ID {}",
                customer.getName(), customer.getSurname(), customer.getId(), customer.getEmail(),
                product.getName(), product.getId());
    }

    @Override
    @Transactional
    public void unsubscribeByCustomerEmail(@NonNull Long productId, @NonNull String email) {
        if (productId < 1L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id can't be less then 1");
        }
        Customer customer = findCustomerByEmail(email);
        Subscription subscription = getSubscriptionByBookId(productId, customer);
        if (subscription == null) {
            log.warn("Can't find any subscriptions for user with email: {} for product with id: {}", email, productId);
            return;
        }
        subscriptionRepo.delete(subscription);
        customer.getSubscriptions().removeIf(subscript -> subscript.getProduct().getId().equals(productId));
        publishSubscriptionEvent("unsubscribed", email, productId);

        log.info("The customer {} {} with ID {} and email {} unsubscribed to new arrives of product with ID {}",
                customer.getName(), customer.getSurname(), customer.getId(), customer.getEmail(), productId);
    }


    private void publishSubscriptionEvent(@NonNull String name, @NonNull String customerEmail, Long productId) {
        if (name.equalsIgnoreCase("subscribed") || name.equalsIgnoreCase("unsubscribed")) {
            applicationEventPublisher.publishEvent(new CustomerSubscriptionEvent(name, customerEmail, productId));
        }
    }

    private Subscription getSubscriptionByBookId(@NonNull Long productId, @NonNull Customer customer) {
        return customer.getSubscriptions().stream()
                .filter(subscription -> subscription.getProduct().getId().equals(productId))
                .findAny().orElse(null);
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Can't find customer with mail: " + email));
    }
}
