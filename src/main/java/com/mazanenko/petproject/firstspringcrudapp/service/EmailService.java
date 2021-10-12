package com.mazanenko.petproject.firstspringcrudapp.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
