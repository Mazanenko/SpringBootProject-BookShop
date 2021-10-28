package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.BookDAO;
import com.mazanenko.petproject.firstspringcrudapp.dao.CustomerDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.CustomerSubscriptionEvent;
import com.mazanenko.petproject.firstspringcrudapp.entity.ProductArrivalEvent;
import com.mazanenko.petproject.firstspringcrudapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final BookDAO bookDAO;
    private final CustomerDAO customerDAO;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, BookDAO bookDAO, CustomerDAO customerDAO) {
        this.javaMailSender = javaMailSender;
        this.bookDAO = bookDAO;
        this.customerDAO = customerDAO;
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
    public void handleArrivalEvent(ProductArrivalEvent event) {
        List<Integer> subscribersList = event.getBook().getSubscribersList();

        subscribersList.forEach(subscriber -> {
            Customer customer = customerDAO.read(subscriber);
            sendSimpleMessage(customer.getEmail(), "New arrival",
                    String.format("Hi! We are glad to tell you, that a book \"%s\" by %s is available to order now.",
                            event.getBook().getName(), event.getBook().getAuthor()));
        });
    }
}
