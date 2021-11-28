package com.mazanenko.petproject.bookshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RESTConsumerForManagerEmailTest {

    private final RESTConsumerForManagerEmail consumer;
    private final String EMAIL = "testloefedadsfwgin@booksland.shop";

    @Autowired
    RESTConsumerForManagerEmailTest(RESTConsumerForManagerEmail consumer) {
        this.consumer = consumer;
    }

    @Test
    void createEmail() {
        try {
            consumer.createEmail(EMAIL, "123efwe456QQ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void changePassword() {
        try {
            consumer.changePassword(EMAIL, "QKSL44585");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteEmail() {
        try {
            consumer.deleteEmail(EMAIL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}