package com.security.fofoqueiro.domain.ports;

public interface IEmailServicePort {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath);
}
