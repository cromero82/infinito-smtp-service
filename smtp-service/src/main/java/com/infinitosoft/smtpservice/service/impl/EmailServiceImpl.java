package com.infinitosoft.smtpservice.service.impl;

import com.infinitosoft.smtpservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${app.mail.from:noreply@example.com}")
    private String defaultFrom;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MailException {
        try {
            if (log.isInfoEnabled()) {
                log.info("Preparando envío de correo: from='{}' -> to='{}' subject='{}'", defaultFrom, to,
                        subject == null ? "" : subject);
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setTo(to);
            helper.setFrom(defaultFrom);
            helper.setSubject(subject == null || subject.isBlank() ? "Mensaje de Infinito SMTP" : subject);
            helper.setText(htmlBody, true);
            if (log.isDebugEnabled()) {
                log.debug("Tamaño del contenido HTML a enviar: {} caracteres", htmlBody == null ? 0 : htmlBody.length());
            }
            mailSender.send(message);
            log.info("Correo enviado correctamente a '{}'", to);
        } catch (MessagingException e) {
            log.error("Error al construir el mensaje de correo para '{}': {}", to, e.getMessage(), e);
            throw new RuntimeException("No se pudo construir o enviar el correo: " + e.getMessage(), e);
        }
    }
}
