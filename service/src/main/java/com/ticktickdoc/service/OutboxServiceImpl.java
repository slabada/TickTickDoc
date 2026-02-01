package com.ticktickdoc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticktickdoc.model.entity.OutboxModel;
import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, NotificationDomain> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Override
    @Transactional
    public OutboxModel save(OutboxModel outbox) {
        return outboxRepository.save(outbox);
    }

    @Override
    @Transactional
    public void sendNotifications() {
        outboxRepository.findForSend(Boolean.FALSE).forEach(outbox -> {
            try {
                NotificationDomain domain = objectMapper.readValue(outbox.getPayload(), NotificationDomain.class);
                kafkaTemplate.send(topicName, String.valueOf(outbox.getId()), domain).get();
                outbox.setSend(Boolean.TRUE);
            } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
                log.error("Error deserializing notification: {}", e.getMessage());
            }
        });
    }
}
