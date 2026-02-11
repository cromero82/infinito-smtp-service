package com.infinitosoft.smtpservice.service.impl;

import com.infinitosoft.smtpservice.service.EmailService;
import com.infinitosoft.smtpservice.util.HtmlTemplateBuilder;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

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
            
            addLogo(helper);

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

    @Override
    public void sendEmailWithAttachment(String to, String subject, String htmlBody, byte[] attachment, String fileName) throws MailException {
        try {
            if (log.isInfoEnabled()) {
                log.info("Preparando envío de correo con adjunto '{}': from='{}' -> to='{}' subject='{}'", 
                        fileName, defaultFrom, to, subject == null ? "" : subject);
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setTo(to);
            helper.setFrom(defaultFrom);
            helper.setSubject(subject == null || subject.isBlank() ? "Mensaje de Infinito SMTP" : subject);
            helper.setText(htmlBody, true);
            
            addLogo(helper);

            if (attachment != null && fileName != null) {
                helper.addAttachment(fileName, new ByteArrayResource(attachment));
            }

            mailSender.send(message);
            log.info("Correo con adjunto '{}' enviado correctamente a '{}'", fileName, to);
        } catch (MessagingException e) {
            log.error("Error al construir el mensaje de correo con adjunto para '{}': {}", to, e.getMessage(), e);
            throw new RuntimeException("No se pudo construir o enviar el correo con adjunto: " + e.getMessage(), e);
        }
    }

    private void addLogo(MimeMessageHelper helper) throws MessagingException {
        ClassPathResource res = new ClassPathResource("assets/logo.png");
        if (res.exists()) {
            helper.addInline("logo", res, "image/png");
            log.info("[DEBUG_LOG] Logo adjuntado correctamente como recurso inline");
        } else {
            log.error("[DEBUG_LOG] No se pudo encontrar el logo en 'assets/logo.png'. El correo se enviará sin logo.");
        }
    }
}
