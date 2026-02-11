package com.infinitosoft.smtpservice.service;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String htmlBody);

    void sendEmailWithAttachment(String to, String subject, String htmlBody, byte[] attachment, String fileName);

}
