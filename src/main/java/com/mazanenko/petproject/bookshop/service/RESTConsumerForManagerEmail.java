package com.mazanenko.petproject.bookshop.service;

public interface RESTConsumerForManagerEmail {

    void createEmail(String email, String password) throws Exception;

    void changePassword(String email, String newPassword) throws Exception;

    void deleteEmail(String email) throws Exception;
}
