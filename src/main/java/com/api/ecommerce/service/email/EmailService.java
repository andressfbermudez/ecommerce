package com.api.ecommerce.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendJWTByEmail(String addressee, String jwt) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(addressee);
        helper.setSubject("Envio de JWT para autenticación en Ecommerce API");

        // Leer y reemplazar
        Path path = Paths.get("src/main/java/com/api/ecommerce/service/email/email-template.html");
        String html = Files.readString(path, StandardCharsets.UTF_8);

        // Reemplazar el marcador por el JWT real
        html = html.replace("{{JWT}}", jwt);

        helper.setText(html, true);
        mailSender.send(message);
    }
}
