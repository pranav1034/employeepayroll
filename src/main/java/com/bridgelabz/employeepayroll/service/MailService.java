package com.bridgelabz.employeepayroll.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to Employee Payroll System");
        message.setText("Dear user, thank you for registering with us. We are glad to have you onboard.");
        mailSender.send(message);
        log.info("mail sent successfully");
    }
}
