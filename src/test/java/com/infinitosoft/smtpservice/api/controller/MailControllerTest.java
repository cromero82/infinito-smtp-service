package com.infinitosoft.smtpservice.api.controller;

import com.infinitosoft.smtpservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    void testSendWithFileEndpoint() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-file.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "contenido del pdf".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/mail/send-file")
                .file(file)
                .param("to", "test@example.com")
                .param("subject", "Prueba con archivo")
                .param("message", "Hola, adjunto archivo")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("accepted"))
                .andExpect(jsonPath("$.fileName").value("test-file.pdf"));

        verify(emailService).sendEmailWithAttachment(
                eq("test@example.com"),
                eq("Prueba con archivo"),
                anyString(),
                any(byte[].class),
                eq("test-file.pdf")
        );
    }
}
