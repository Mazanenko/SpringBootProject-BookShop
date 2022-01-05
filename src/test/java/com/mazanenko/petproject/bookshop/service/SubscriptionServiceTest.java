package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.repository.SubscriptionRepository;
import com.mazanenko.petproject.bookshop.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    SubscriptionService subscriptionService = new SubscriptionServiceImpl();

    @Mock
    private SubscriptionRepository subscriptionRepo;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookService bookService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private final String EMAIL = "test_customer@mail.ru";
    private Long PRODUCT_ID = 1L;
    private final Book BOOK = new Book(1L, "name", "description", 3, 5,
            "author", "genre", "url");
    private final DeliveryAddress DELIVERY_ADDRESS = new DeliveryAddress(1L, "country", "city",
            "street", 1, "note");
    private final Cart CART = new Cart();
    private final Customer CUSTOMER = new Customer(1L, "Name", "Surname", "male",
            "12345", EMAIL, "password", DELIVERY_ADDRESS, CART);

    @BeforeEach
    void setUp() {
        CART.setId(1L);
    }

    @Test
    void subscribeByCustomerEmailShouldSubscribeToBook() {
        //Given product id and email
        //When
        Mockito.when(customerService.getCustomerByEmail(EMAIL)).thenReturn(CUSTOMER);
        Mockito.when(bookService.getBookById(PRODUCT_ID)).thenReturn(BOOK);
        subscriptionService.subscribeByCustomerEmail(PRODUCT_ID, EMAIL);
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        ArgumentCaptor<ApplicationEventPublisher> captor = ArgumentCaptor.forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(subscriptionRepo, Mockito.times(1))
                .save(subscriptionArgumentCaptor.capture());
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(captor.capture());
    }

    @Test
    void subscribeByCustomerEmailWhenEmailIsNullOrProductIdLessThanOne() {
        //Given product id = 0 and email = null
        //When
        subscriptionService.subscribeByCustomerEmail(0L, null);

        //Then
        Mockito.verifyNoInteractions(customerService);
        Mockito.verifyNoInteractions(bookService);
        Mockito.verifyNoInteractions(subscriptionRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void subscribeByCustomerEmailWhenCustomerOrBookIsNull() {
        //Given customer and book are null
        //When
        Mockito.when(customerService.getCustomerByEmail(EMAIL)).thenReturn(null);
        Mockito.when(bookService.getBookById(PRODUCT_ID)).thenReturn(null);
        subscriptionService.subscribeByCustomerEmail(PRODUCT_ID, EMAIL);

        //Then
        Mockito.verifyNoInteractions(subscriptionRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void unsubscribeByCustomerEmailShouldUnsubscribeToBook() {
        //Given product id and email
        Subscription subscription = new Subscription(BOOK, CUSTOMER);
        List<Subscription> list = new ArrayList<>();
        list.add(subscription);

        CUSTOMER.setSubscriptions(list);

        //When
        Mockito.when(customerService.getCustomerByEmail(EMAIL)).thenReturn(CUSTOMER);

        subscriptionService.unsubscribeByCustomerEmail(PRODUCT_ID, EMAIL);
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        ArgumentCaptor<ApplicationEventPublisher> captor = ArgumentCaptor.forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(subscriptionRepo, Mockito.times(1))
                .delete(subscriptionArgumentCaptor.capture());
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(captor.capture());
    }

    @Test
    void unsubscribeByCustomerEmailWhenEmailIsNullOrProductIdLessThanOne() {
        //Given product id = 0 and email = null
        //When
        subscriptionService.unsubscribeByCustomerEmail(0L, null);

        //Then
        Mockito.verifyNoInteractions(customerService);
        Mockito.verifyNoInteractions(subscriptionRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void unsubscribeByCustomerEmailWhenCustomerIsNull() {
        //Given customer is null
        //When
        Mockito.when(customerService.getCustomerByEmail(EMAIL)).thenReturn(null);
        subscriptionService.subscribeByCustomerEmail(PRODUCT_ID, EMAIL);

        //Then
        Mockito.verifyNoInteractions(subscriptionRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void unsubscribeByCustomerEmailWhenCustomerDoNotSubscribedToThisBook() {
        //Given product id and email
        Subscription subscription = new Subscription(BOOK, CUSTOMER);
        List<Subscription> list = new ArrayList<>();
        list.add(subscription);

        CUSTOMER.setSubscriptions(list);
        Long newProductId = ++PRODUCT_ID;

        //When
        Mockito.when(customerService.getCustomerByEmail(EMAIL)).thenReturn(CUSTOMER);
        subscriptionService.unsubscribeByCustomerEmail(newProductId, EMAIL);

        //Then
        Mockito.verifyNoInteractions(subscriptionRepo);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }
}