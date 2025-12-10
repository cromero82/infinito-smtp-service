package com.infinitosoft.smtpservice.api.controller;

import com.infinitosoft.smtpservice.api.dto.EmailRequest;
import com.infinitosoft.smtpservice.service.EmailService;
import com.infinitosoft.smtpservice.util.HtmlTemplateBuilder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@Valid @RequestBody EmailRequest request) {
        log.info("Solicitud de envío de correo recibida: to='{}', subject='{}'", request.getTo(),
                request.getSubject() == null ? "" : request.getSubject());
        String subject = request.getSubject();
        String html = HtmlTemplateBuilder.buildEmail(subject, request.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("HTML generado para envío. Longitud={} caracteres", html.length());
        }
        emailService.sendHtmlEmail(request.getTo(), subject, html);
        log.info("Solicitud de envío procesada correctamente para to='{}'", request.getTo());
        Map<String, Object> body = new HashMap<>();
        body.put("status", "accepted");
        body.put("to", request.getTo());
        body.put("subject", subject == null || subject.isBlank() ? "Mensaje de Infinito SMTP" : subject);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
    }
}
