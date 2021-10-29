package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.BookDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.CustomerRegistrationEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.CustomerSubscriptionEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.OrderEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import com.mazanenko.petproject.firstspringcrudapp.service.EmailService;
import com.mazanenko.petproject.firstspringcrudapp.service.ManagerService;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final BookDAO bookDAO;
    private final CustomerService customerService;
    private final ManagerService managerService;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, BookDAO bookDAO, CustomerService customerService,
                            ManagerService managerService) {
        this.javaMailSender = javaMailSender;
        this.bookDAO = bookDAO;
        this.customerService = customerService;
        this.managerService = managerService;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
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
        Customer customer = customerService.getCustomerByEmail(event.getCustomerEmail());

        String message = String.format("Hello, %s! \n" + "Welcome to Booksland! Please, visit next link: " +
                        "http://localhost:8080/customer/activate/%s to activate your account and complete registration."
                , customer.getName(), customer.getActivationCode());

        sendSimpleMessage(customer.getEmail(), "Registration on booksland", message);
    }

    @Async
    @EventListener
    @Override
    public void handleSubscriptionEvent(CustomerSubscriptionEvent event) {
        Book book = bookDAO.read(event.getProductId());

        switch (event.getName()) {

            case "subscribed":
                sendSimpleMessage(event.getCustomerEmail(), "Subscription",
                        String.format("Hi! You will be notified when the book \"%s\" by %s arrives at the warehouse.",
                                book.getName(), book.getAuthor()));
                break;

            case "unsubscribed":
                sendSimpleMessage(event.getCustomerEmail(), "Subscription",
                        String.format("Hi! You have opted out of receiving notifications of arrival " +
                                "of book \"%s\" by %s at the warehouse.", book.getName(), book.getAuthor()));
                break;
        }
    }

    @Async
    @EventListener
    @Override
    public void handleArrivalEvent(ProductArrivalEvent event) {
        List<Integer> subscribersList = event.getBook().getSubscribersList();

        subscribersList.forEach(subscriber -> {
            Customer customer = customerService.getCustomerById(subscriber);
            sendSimpleMessage(customer.getEmail(), "New arrival",
                    String.format("Hi! We are glad to tell you, that a book \"%s\" by %s is available to order now.",
                            event.getBook().getName(), event.getBook().getAuthor()));
        });
    }

    @Async
    @EventListener
    @Override
    public void handleOrderEvent(OrderEvent event) {
        AtomicInteger i = new AtomicInteger();
        Cart cart = event.getCart();
        Customer customer = customerService.getCustomerById(cart.getCustomerId());
        List<String> list = new ArrayList<>();
        List<Manager> managerList = managerService.getAllManagers();

        cart.getOrderList().forEach(order -> list.add(i.incrementAndGet() + ". " + order.getBook().getName() + " by " +
                order.getBook().getAuthor() + " - " + order.getQuantity() + " pc."));

        String messageForCustomer = "Hi! Your order: \n" + StringUtils.join(list, '\n') +
                "\n Our manager will contact you as soon as possible to clarify the details of payment and delivery";

        String messageForManager = "Finally! Someone made new order. \n\nHere is customer's contact info:\n"
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
                + StringUtils.join(list, '\n') +
                "\n\nPlease, contact to customer as soon as possible to clarify the details of payment and delivery.";

        // sending message to customer
        sendSimpleMessage(customer.getEmail(), "The order at Booksland", messageForCustomer);

        // sending message to managers
        if(managerList != null) {
            managerList.forEach(manager -> sendSimpleMessage(manager.getEmail(),
                    "Alarm! New order.", messageForManager));
        }
    }
}
