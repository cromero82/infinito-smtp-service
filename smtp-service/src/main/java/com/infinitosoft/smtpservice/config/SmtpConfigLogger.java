package com.infinitosoft.smtpservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * TEMPORAL: Log de diagnóstico para verificar que la app lee la variable de entorno SMTP_PASSWORD.
 * IMPORTANTE: Esto imprime la contraseña en los logs INTENCIONALMENTE a petición del usuario.
 * No debe mantenerse en producción. Eliminar cuando finalicen las pruebas.
 */
@Component
public class SmtpConfigLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SmtpConfigLogger.class);

    private final Environment env;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${app.mail.from:}")
    private String from;

    public SmtpConfigLogger(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Lee directamente del entorno del SO
        String envPw = System.getenv("SMTP_PASSWORD");
        // Lee el valor que Spring resolvió para la propiedad (puede provenir del entorno)
        String propPw = env.getProperty("spring.mail.password");

        log.warn("[DEBUG TEMP] SMTP_USERNAME: '{}'", username);
        log.warn("[DEBUG TEMP] MAIL_FROM: '{}'", from);
        log.warn("[DEBUG TEMP] ENV SMTP_PASSWORD: '{}'", envPw);
        log.warn("[DEBUG TEMP] spring.mail.password (tras resolución de propiedades): '{}'", propPw);
        log.warn("[DEBUG TEMP] ATENCIÓN: Estas líneas contienen credenciales y son SOLO para pruebas. Eliminar después de verificar.");
    }
}
