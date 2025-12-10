package com.infinitosoft.smtpservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmtpServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(SmtpServiceApplication.class);

    public static void main(String[] args) {
        log.info("Iniciando Infinito SMTP Service...");
        SpringApplication.run(SmtpServiceApplication.class, args);
        log.info("Infinito SMTP Service iniciado correctamente.");
    }

}
