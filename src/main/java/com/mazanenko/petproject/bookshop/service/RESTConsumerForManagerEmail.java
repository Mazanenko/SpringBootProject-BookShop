package com.mazanenko.petproject.bookshop.service;

public interface RESTConsumerForManagerEmail {

    boolean createEmail(String email, String password) throws Exception;

    boolean changePassword(String email, String newPassword) throws Exception;

    void deleteEmail(String email) throws Exception;
}
