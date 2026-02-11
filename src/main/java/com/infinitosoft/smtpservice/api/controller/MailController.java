package com.infinitosoft.smtpservice.api.controller;

import com.infinitosoft.smtpservice.api.dto.EmailRequest;
import com.infinitosoft.smtpservice.service.EmailService;
import com.infinitosoft.smtpservice.util.HtmlTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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

    @PostMapping(value = "/send-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> sendWithFile(
            @RequestParam("to") String to,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam("message") String message,
            @RequestParam("file") MultipartFile file) {

        log.info("Solicitud de envío de correo con archivo recibida: to='{}', subject='{}', fileName='{}'",
                to, subject == null ? "" : subject, file.getOriginalFilename());

        String html = HtmlTemplateBuilder.buildEmail(subject, message);

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            log.error("Error al leer el archivo adjunto", e);
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "No se pudo leer el archivo adjunto");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            fileName = "archivo_adjunto";
        }

        emailService.sendEmailWithAttachment(to, subject, html, fileBytes, fileName);

        log.info("Solicitud de envío con archivo '{}' procesada correctamente para to='{}'", fileName, to);
        Map<String, Object> body = new HashMap<>();
        body.put("status", "accepted");
        body.put("to", to);
        body.put("fileName", fileName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
    }
}
