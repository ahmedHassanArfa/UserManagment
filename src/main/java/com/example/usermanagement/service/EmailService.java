package com.example.usermanagement.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
