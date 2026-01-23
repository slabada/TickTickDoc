package com.ticktickdoc.scheduled;

import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.notification.domain.NotificationDocumentDomain;
import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.service.DocumentService;
import com.ticktickdoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationScheduled {

    private final UserService userService;
    private final DocumentService documentService;

    private final KafkaTemplate<String, NotificationDomain> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Scheduled(cron = "0 0 12 * * *")
    private void getDocumentFotNotification() {
        long notificationDay = 3L;
        List<DocumentModel> documents = documentService.findAllByDateExecution(LocalDate.now().plusDays(notificationDay));

        Map<Long, List<DocumentModel>> documentsByAuthor = documents.stream()
                .collect(Collectors.groupingBy(DocumentModel::getLinkAuthorId));

        for (Map.Entry<Long, List<DocumentModel>> entry : documentsByAuthor.entrySet()) {
            Long authorId = entry.getKey();
            List<DocumentModel> authorDocuments = entry.getValue();

            UserDomain user = userService.getUser(authorId);

            List<NotificationDocumentDomain> notificationDocumentDomains = new LinkedList<>();
            for (var document : authorDocuments) {
                NotificationDocumentDomain notification = new NotificationDocumentDomain();
                notification.setName(document.getName());
                notification.setDateExecution(document.getDateExecution());
                notificationDocumentDomains.add(notification);
            }

            NotificationDomain notification = new NotificationDomain();
            notification.setEmail(user.getEmail());
            notification.setDocument(notificationDocumentDomains);

            kafkaTemplate.send(topicName, notification);
        }
    }

}
