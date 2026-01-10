package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService implements Notification {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    @KafkaListener(topics = "${kafka.topic.name}")
    @Override
    public void send(NotificationDomain domain) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(email);
            helper.setTo(domain.getEmail());
            String htmlContent = loadHtmlTemplate(domain);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("error when sending a message to the mail: ", e);
        }
    }

    private String loadHtmlTemplate(NotificationDomain domain) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/notificationDocument.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        StringBuilder documentRows = new StringBuilder();
        for (var documents : domain.getDocument()) {
            documentRows.append("<tr>")
                    .append("<td>").append(documents.getName()).append("</td>")
                    .append("<td>").append(documents.getDateExecution()).append("</td>")
                    .append("</tr>");
        }
        template = template
                .replace("{{notification_title}}", "Уведомление")
                .replace("{{tasks_rows}}", documentRows.toString());
        return template;
    }
}
