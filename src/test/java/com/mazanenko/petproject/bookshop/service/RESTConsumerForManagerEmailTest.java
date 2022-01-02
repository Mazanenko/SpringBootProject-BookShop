package com.mazanenko.petproject.bookshop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RESTConsumerForManagerEmailTest {

    private final RESTConsumerForManagerEmail consumer;
    private final String EMAIL = "test@booksland.shop";

    @Autowired
    RESTConsumerForManagerEmailTest(RESTConsumerForManagerEmail consumer) {
        this.consumer = consumer;
    }

    @Test
    void createEmailShouldCreateNewEmail() throws Exception {
        //Before creating new Email, should delete already exist
        deleteEmail();

        //Given
        String pass = "123efwe456QQ";

        //When method will execute, return value should be true
        Assertions.assertTrue(consumer.createEmail(EMAIL, pass));
    }

    @Test
    void createEmailShouldThrowExceptionWhenEmailOrPasswordIsNull() {
        //Given
        String email = null;
        String pass = null;
        String exceptionMessage = "Email or password is null";

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.createEmail(email, pass));

        //Then
        Assertions.assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    void createEmailShouldThrowExceptionWhenEmailAlreadyExist() {
        //Given
        String pass = "123efwe456QQ";
        String exceptionMessage = "Error while trying registered email";
        createEmail(pass);

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.createEmail(EMAIL, pass));

        //Then
        Assertions.assertTrue(exception.getMessage().contains(exceptionMessage));
    }

    @Test
    void changePasswordShouldChangePasswordForExistingEmail() throws Exception {
        //Given
        String pass = "123efwe456QQ";
        String newPass = "QKSL44585556";
        createEmail(pass);

        //When method will execute, return value should be true
        Assertions.assertTrue(consumer.changePassword(EMAIL, newPass));
    }

    @Test
    void changePasswordShouldThrowExceptionWhenEmailOrPasswordIsNull() {
        //Given
        String email = null;
        String newPass = null;
        String exceptionMessage = "Email or new password is null";

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.changePassword(email, newPass));

        //Then
        Assertions.assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    void changePasswordShouldThrowExceptionWhenNewPasswordIsInvalid() {
        //Given
        String pass = "123efwe456QQ";
        String newPass = "123456";
        String exceptionMessage = "Error while trying to change password for email";
        createEmail(pass);

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.changePassword(EMAIL, newPass));

        //Then
        Assertions.assertTrue(exception.getMessage().contains(exceptionMessage));
    }

    @Test
    void deleteEmailShouldThrowExceptionWhenGivenEmailIsNull() {
        //Given
        String email = null;
        String exceptionMessage = "Email is null";

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.deleteEmail(email));

        //Then
        Assertions.assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    void deleteEmailShouldThrowExceptionWhenGivenEmailIsNotExist() {
        //Given
        String email = "notexist@mail.com";
        String exceptionMessage = "Error while trying to delete email";

        //When
        Exception exception = Assertions.assertThrows(Exception.class, () -> consumer.deleteEmail(email));

        //Then
        Assertions.assertTrue(exception.getMessage().contains(exceptionMessage));
    }


    private void createEmail(String pass) {
        try {
            consumer.createEmail(EMAIL, pass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteEmail() {
        try {
            consumer.deleteEmail(EMAIL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}