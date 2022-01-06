package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.*;
import com.mazanenko.petproject.bookshop.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.bookshop.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.bookshop.entity.event.OrderEvent;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.bookshop.service.BookService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import com.mazanenko.petproject.bookshop.service.EmailService;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;
    private CustomerService customerService;
    private ManagerService managerService;
    private BookService bookService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String username;

    public EmailServiceImpl() {
    }

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, CustomerService customerService,
                            ManagerService managerService, BookService bookService) {
        this.javaMailSender = javaMailSender;
        this.customerService = customerService;
        this.managerService = managerService;
        this.bookService = bookService;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        if (to == null) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    @EventListener
    @Override
    public void handleCustomerRegistrationEvent(CustomerRegistrationEvent event) {
        if (event == null) {
            return;
        }

        Customer customer = event.getCustomer();
        if (customer == null) {
            return;
        }

        String message = String.format("Hello, %s! \n" + "Welcome to Booksland! Please, visit next link: " +
                        "http://localhost:8080/customer/activate/%s to activate your account and complete registration."
                , customer.getName(), customer.getActivationCode());

        sendSimpleMessage(customer.getEmail(), "Registration on booksland", message);

        LOGGER.info("Sent activation message to customer {} {} with ID {} and email {}", customer.getName(),
                customer.getSurname(), customer.getId(), customer.getEmail());
    }

    @Async
    @EventListener
    @Override
    public void handleSubscriptionEvent(CustomerSubscriptionEvent event) {
        if (event == null) {
            return;
        }

        Book book = bookService.getBookById(event.getProductId());
        if (book == null) {
            return;
        }

        switch (event.getName().toLowerCase(Locale.ROOT)) {
            case "subscribed":
                sendSimpleMessage(event.getCustomerEmail(), "Subscription",
                        String.format("Hi! You will be notified when the book \"%s\" by %s arrives at the warehouse.",
                                book.getName(), book.getAuthor()));

                LOGGER.info("sent message about subscription event for book {} by {} to customer email {}",
                        book.getName(), book.getAuthor(), event.getCustomerEmail());
                break;

            case "unsubscribed":
                sendSimpleMessage(event.getCustomerEmail(), "Unsubscription",
                        String.format("Hi! You have opted out of receiving notifications of arrival " +
                                "of book \"%s\" by %s at the warehouse.", book.getName(), book.getAuthor()));

                LOGGER.info("sent message about unsubscription event for book {} by {} to customer email {}",
                        book.getName(), book.getAuthor(), event.getCustomerEmail());
                break;

            default: LOGGER.warn("Can't send message about subscription event for book ({} by {}) to customer (email {}), " +
                            "because of illegal argument in event's name ({})", book.getName(),
                    book.getAuthor(), event.getCustomerEmail(), event.getName());
        }
    }

    @Async
    @EventListener
    @Override
    public void handleArrivalEvent(ProductArrivalEvent event) {
        if (event == null) {
            return;
        }
        List<Subscription> subscriptions = event.getBook().getSubscribersList();

        subscriptions.forEach(subscriber -> {
            Customer customer = customerService.getCustomerById(subscriber.getCustomer().getId());
            sendSimpleMessage(customer.getEmail(), "New arrival",
                    String.format("Hi! We are glad to tell you, that a book \"%s\" by %s is available to order now.",
                            event.getBook().getName(), event.getBook().getAuthor()));

            LOGGER.info("Sent message about new arrival for book {} by {} to customer with email {}",
                    event.getBook().getName(), event.getBook().getAuthor(), customer.getEmail());
        });
    }

    @Async
    @EventListener
    @Override
    public void handleOrderEvent(OrderEvent event) {
        if (event == null) {
            return;
        }

        AtomicInteger i = new AtomicInteger();
        Cart cart = event.getCart();
        Customer customer = cart.getCustomer();
        List<String> orderList = new ArrayList<>();
        List<Manager> managerList = managerService.getAllManagers();

        // filling list of orders
        cart.getOrderList().forEach(order -> orderList.add(i.incrementAndGet() + ". " + order.getBook().getName() +
                " by " + order.getBook().getAuthor() + " - " + order.getQuantity() + " pc."));

        String messageForCustomer = createMessageAboutOrderEventForCustomer(orderList);
        String messageForManager = createMessageAboutOrderEventForManager(customer, orderList);

        // sending message to customer
        sendSimpleMessage(customer.getEmail(), "The order at Booksland", messageForCustomer);

        // sending message to managers
        if(managerList != null) {
            managerList.forEach(manager -> sendSimpleMessage(manager.getEmail(),
                    "Alarm! New order", messageForManager));
        }
        LOGGER.info("Sent message about customer with email {} made an order {}", customer.getEmail(),
                orderList);
    }

    private String createMessageAboutOrderEventForCustomer(List<String> orderList) {
        if (orderList == null) {
            return null;
        }
        return "Hi! Your order: \n" + StringUtils.join(orderList, '\n') +
                "\n Our manager will contact you as soon as possible to clarify the details of payment and delivery";
    }

    private String createMessageAboutOrderEventForManager(Customer customer, List<String> orderList) {
        if (customer == null || orderList == null) {
            return null;
        }
        return "Finally! Someone made new order. \n\nHere is customer's contact info:\n"
                + "Name: " + customer.getName() + " " + customer.getSurname() + '\n'
                + "Email: " + customer.getEmail() + '\n'
                + "Phone: " + customer.getPhone() + '\n' + '\n'
                + "Address: " + '\n'
                + "Country: " + customer.getDeliveryAddress().getCountry() + '\n'
                + "City: " + customer.getDeliveryAddress().getCity() + '\n'
                + "Street: " + customer.getDeliveryAddress().getStreet() + '\n'
                + "House number: " + customer.getDeliveryAddress().getHouseNumber() + '\n'
                + "Note: " + customer.getDeliveryAddress().getNote() + '\n' + '\n'
                + "Order list: \n"
                + StringUtils.join(orderList, '\n') +
                "\n\nPlease, contact to customer as soon as possible to clarify the details of payment and delivery.";
    }
}
