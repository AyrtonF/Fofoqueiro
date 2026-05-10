package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.ports.IEmailServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Component
@RequiredArgsConstructor
public class EmailServiceAdapterImpl implements IEmailServicePort {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            File attachment = new File(attachmentPath);
            if (attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            } else {
                // Log or handle the case where the attachment file does not exist
                System.err.println("Attachment file not found: " + attachmentPath);
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Log the exception
            System.err.println("Error sending email with attachment: " + e.getMessage());
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
