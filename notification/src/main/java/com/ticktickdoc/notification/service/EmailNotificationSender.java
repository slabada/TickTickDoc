package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.notification.enums.NotificationTypeEnum;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service(NotificationTypeEnum.EMAIL_VALUE)
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void send(String userEmail, NotificationDomain domain) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(email);
            helper.setTo(userEmail);
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
        String documentRows = "<tr>" +
                "<td>" + domain.getDocument().getName() + "</td>" +
                "<td>" + domain.getDocument().getDateExecution() + "</td>" +
                "</tr>";
        template = template
                .replace("{{notification_title}}", "Уведомление")
                .replace("{{tasks_rows}}", documentRows);
        return template;
    }
}
