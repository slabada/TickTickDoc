package com.ticktickdoc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticktickdoc.model.entity.OutboxModel;
import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.repository.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxServiceImplTest {

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaTemplate<String, NotificationDomain> kafkaTemplate;

    @InjectMocks
    private OutboxServiceImpl outboxService;

    private OutboxModel outboxModel;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(outboxService, "topicName", "test-topic");
        outboxModel = OutboxModel.builder()
                .id(UUID.randomUUID())
                .payload("{\"message\":\"hello\"}")
                .send(false)
                .build();
    }

    @Test
    void save() {
        when(outboxRepository.save(outboxModel)).thenReturn(outboxModel);

        OutboxModel result = outboxService.save(outboxModel);

        assertNotNull(result);
        verify(outboxRepository).save(outboxModel);
    }

    @Test
    void sendNotifications() throws Exception {
        NotificationDomain notificationDomain = new NotificationDomain();

        when(outboxRepository.findForSend(false))
                .thenReturn(List.of(outboxModel));

        when(objectMapper.readValue(outboxModel.getPayload(), NotificationDomain.class))
                .thenReturn(notificationDomain);

        CompletableFuture<SendResult<String, NotificationDomain>> future = CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send("test-topic", String.valueOf(outboxModel.getId()), notificationDomain))
                .thenReturn(future);

        outboxService.sendNotifications();

        verify(kafkaTemplate).send("test-topic", String.valueOf(outboxModel.getId()), notificationDomain);
        assertTrue(outboxModel.getSend());
    }
}