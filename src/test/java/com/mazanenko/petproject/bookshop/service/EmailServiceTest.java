package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.bookshop.entity.event.OrderEvent;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.bookshop.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService = new EmailServiceImpl();

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private CustomerService customerService;
    @Mock
    private ManagerService managerService;
    @Mock
    private BookService bookService;

    private final ArgumentCaptor<SimpleMailMessage> CAPTOR = ArgumentCaptor.forClass(SimpleMailMessage.class);

    private final Book BOOK = new Book(1L, "name", "description", 3, 5,
            "author", "genre", "url");
    private final Cart CART = new Cart();
    private final Order ORDER = new Order(CART, BOOK, 3);
    private final DeliveryAddress DELIVERY_ADDRESS = new DeliveryAddress(1L, "country", "city",
            "street", 1, "note");
    private final Customer CUSTOMER = new Customer(1L, "Name", "Surname", "male",
            "12345", "test_customer@mail.ru", "password", DELIVERY_ADDRESS, CART);
    private final CustomerRegistrationEvent REGISTRATION_EVENT = new CustomerRegistrationEvent(CUSTOMER);
    private final ProductArrivalEvent ARRIVAL_EVENT = new ProductArrivalEvent(BOOK);
    private final OrderEvent ORDER_EVENT = new OrderEvent(CART);


    @BeforeEach
    void setUp() {
        CART.setId(1L);
        CART.setCustomer(CUSTOMER);
        ORDER.setId(1L);
    }

    @Test
    void sendSimpleMessageShouldSendMessage() {
        //Given
        String to = "email@mail.com";
        String subject = "subject";
        String message = "message";

        //When
        emailService.sendSimpleMessage(to, subject, message);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertTrue(Arrays.asList(Objects.requireNonNull(CAPTOR.getValue().getTo())).
                contains(to));
        Assertions.assertEquals(subject, CAPTOR.getValue().getSubject());
        Assertions.assertEquals(message, CAPTOR.getValue().getText());
    }

    @Test
    void sendSimpleMessageShouldNotSendMessageWhenEmailAddressIsNull() {
        //Given to = null
        String subject = "subject";
        String message = "message";

        //When
        emailService.sendSimpleMessage(null, subject, message);

        //Then
        Mockito.verifyNoInteractions(javaMailSender);
    }

    @Test
    void handleCustomerRegistrationEventShouldSendActivationMessage() {
        //Given registrationEvent
        //When
        emailService.handleCustomerRegistrationEvent(REGISTRATION_EVENT);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertTrue(Arrays.asList(Objects.requireNonNull(CAPTOR.getValue().getTo())).
                contains(CUSTOMER.getEmail()));
    }

    @Test
    void handleCustomerRegistrationEventShouldNotSendActivationMessageWhenEventIsNull() {
        //Given registrationEvent = null
        //When
        emailService.handleCustomerRegistrationEvent(null);

        //Then
        Mockito.verifyNoInteractions(javaMailSender);
    }

    @Test
    void handleSubscriptionEventShouldSendMessageAboutSubscribing() {
        //Given
        String subject = "Subscription";
        CustomerSubscriptionEvent subscriptionEvent = new CustomerSubscriptionEvent("subscribed",
                CUSTOMER.getEmail(), BOOK.getId());

        //When
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        emailService.handleSubscriptionEvent(subscriptionEvent);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertEquals(subject, CAPTOR.getValue().getSubject());
        Assertions.assertTrue(Arrays.asList(Objects.requireNonNull(CAPTOR.getValue().getTo())).
                contains(CUSTOMER.getEmail()));
    }

    @Test
    void handleSubscriptionEventShouldSendMessageAboutUnsubscribing() {
        //Given
        String subject = "Unsubscription";
        CustomerSubscriptionEvent subscriptionEvent = new CustomerSubscriptionEvent("unsubscribed",
                CUSTOMER.getEmail(), BOOK.getId());

        //When
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        emailService.handleSubscriptionEvent(subscriptionEvent);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertEquals(subject, CAPTOR.getValue().getSubject());
        Assertions.assertTrue(Arrays.asList(Objects.requireNonNull(CAPTOR.getValue().getTo())).
                contains(CUSTOMER.getEmail()));
    }

    @Test
    void handleSubscriptionEventShouldNotSendAnyMessageWhenEventNameIncorrect() {
        //Given
        CustomerSubscriptionEvent subscriptionEvent = new CustomerSubscriptionEvent("incorrectName",
                CUSTOMER.getEmail(), BOOK.getId());

        //When
        Mockito.when(bookService.getBookById(BOOK.getId())).thenReturn(BOOK);
        emailService.handleSubscriptionEvent(subscriptionEvent);

        //Then
        Mockito.verifyNoInteractions(javaMailSender);
    }

    @Test
    void handleSubscriptionEventShouldNotSendAnyMessageWhenEventIsNull() {
        //Given event = null
        //When
        emailService.handleSubscriptionEvent(null);

        //Then
        Mockito.verifyNoInteractions(javaMailSender);
        Mockito.verifyNoInteractions(bookService);
    }

    @Test
    void handleArrivalEventShouldSendMessageAboutNewArrival() {
        //Given
        Subscription subscription = new Subscription(BOOK, CUSTOMER);
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(subscription);
        BOOK.setSubscribersList(subscriptions);
        String subject = "New arrival";

        //When
        Mockito.when(customerService.getCustomerById(CUSTOMER.getId())).thenReturn(CUSTOMER);
        emailService.handleArrivalEvent(ARRIVAL_EVENT);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertEquals(subject, CAPTOR.getValue().getSubject());
    }

    @Test
    void handleArrivalEventShouldNotSendMessageAboutNewArrivalWhenSubscriptionsListIsEmpty() {
        //Given
        List<Subscription> subscriptions = new ArrayList<>();
        BOOK.setSubscribersList(subscriptions);

        //When
        emailService.handleArrivalEvent(ARRIVAL_EVENT);

        //Then
        Mockito.verifyNoInteractions(customerService);
        Mockito.verifyNoInteractions(javaMailSender);
    }

    @Test
    void handleArrivalEventShouldNotSendMessageAboutNewArrivalWhenEventIsNull() {
        //Given event = null
        //When
        emailService.handleArrivalEvent(null);

        //Then
        Mockito.verifyNoInteractions(customerService);
        Mockito.verifyNoInteractions(javaMailSender);
    }

    @Test
    void handleOrderEventShouldSendMessagesAboutOrdering() {
        //Given
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        Manager manager = new Manager(1L, "manager", "manager", "manager_email@mail.com",
                "password");
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        //When
        Mockito.when(managerService.getAllManagers()).thenReturn(managers);
        emailService.handleOrderEvent(ORDER_EVENT);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(2)).send(CAPTOR.capture());
        Assertions.assertTrue(CAPTOR.getAllValues().stream()
                .anyMatch(v -> Objects.equals(v.getSubject(), "The order at Booksland")));
        Assertions.assertTrue(CAPTOR.getAllValues().stream()
                .anyMatch(v -> Objects.equals(v.getSubject(), "Alarm! New order")));
    }

    @Test
    void handleOrderEventShouldSendMessagesAboutOrderingWhenManagersListIsEmpty() {
        //Given
        List<Order> orderList = new ArrayList<>();
        orderList.add(ORDER);
        CART.setOrderList(orderList);

        //When
        Mockito.when(managerService.getAllManagers()).thenReturn(null);
        emailService.handleOrderEvent(ORDER_EVENT);

        //Then
        Mockito.verify(javaMailSender, Mockito.times(1)).send(CAPTOR.capture());
        Assertions.assertTrue(CAPTOR.getAllValues().stream()
                .anyMatch(v -> Objects.equals(v.getSubject(), "The order at Booksland")));
        Assertions.assertFalse(CAPTOR.getAllValues().stream()
                .anyMatch(v -> Objects.equals(v.getSubject(), "Alarm! New order")));
    }

    @Test
    void handleOrderEventShouldNotSendMessagesAboutOrderingWhenEventIsNull() {
        //Given event = null
        //When
        emailService.handleOrderEvent(null);

        //Then
        Mockito.verifyNoInteractions(javaMailSender);
        Mockito.verifyNoInteractions(managerService);
    }
}