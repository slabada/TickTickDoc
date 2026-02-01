package com.ticktickdoc.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.model.entity.OutboxModel;
import com.ticktickdoc.notification.domain.NotificationDocumentDomain;
import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.notification.domain.NotificationTypeDomain;
import com.ticktickdoc.service.DocumentService;
import com.ticktickdoc.service.OutboxService;
import com.ticktickdoc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ticktickdoc.notification.enums.NotificationTypeEnum.EMAIL;
import static com.ticktickdoc.notification.enums.NotificationTypeEnum.TELEGRAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduled {

    private final UserService userService;
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    private final OutboxService outboxService;

    @Scheduled(cron = "0 0 11 * * *")
    public void getDocumentFotNotification() {
        long notificationDay = 0L;
        List<DocumentModel> documents = documentService.findAllByDateExecution(LocalDate.now().plusDays(notificationDay));

        for (var document : documents) {
            UserDomain user = userService.getUser(document.getLinkAuthorId());

            NotificationDocumentDomain notificationDocument = new NotificationDocumentDomain();
            notificationDocument.setName(document.getName());
            notificationDocument.setDateExecution(document.getDateExecution());

            NotificationDomain notification = new NotificationDomain();

            Set<NotificationTypeDomain> notificationTypeDomains = new HashSet<>();

            for (var type : user.getNotificationType()) {
                switch (type) {
                    case EMAIL -> {
                        NotificationTypeDomain notificationTypeDomain = new NotificationTypeDomain();
                        notificationTypeDomain.setType(EMAIL);
                        notificationTypeDomain.setPayload(user.getEmail());
                        notificationTypeDomains.add(notificationTypeDomain);
                    }
                    case TELEGRAM -> {
                        NotificationTypeDomain notificationTypeDomain = new NotificationTypeDomain();
                        notificationTypeDomain.setType(TELEGRAM);
                        notificationTypeDomain.setPayload(user.getTelegram());
                        notificationTypeDomains.add(notificationTypeDomain);
                    }
                }
            }

            notification.setNotificationType(notificationTypeDomains);
            notification.setDocument(notificationDocument);

            try {
                String json = objectMapper.writeValueAsString(notification);

                OutboxModel outbox = OutboxModel.builder()
                        .payload(json)
                        .send(Boolean.FALSE)
                        .createdAt(LocalDateTime.now())
                        .build();

                outboxService.save(outbox);
            } catch (JsonProcessingException e) {
                log.error("Error serializing notification: {}", e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void notifyByKafka() {
        outboxService.sendNotifications();
    }
}
