package com.ticktickdoc.notification.proxy;

import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.notification.model.ProcessEventModel;
import com.ticktickdoc.notification.repository.ProcessEventRepository;
import com.ticktickdoc.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class NotificationServiceProxy implements NotificationService {

    private final NotificationService notificationService;
    private final ProcessEventRepository processEventRepository;

    public NotificationServiceProxy(
            @Qualifier("notificationServiceImpl") NotificationService notificationService,
            ProcessEventRepository processEventRepository) {
        this.notificationService = notificationService;
        this.processEventRepository = processEventRepository;
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.name}")
    public void process(ConsumerRecord<String, NotificationDomain> record) {
        Optional<ProcessEventModel> byProcessEventId = processEventRepository.findByProcessEventId(UUID.fromString(record.key()));
        if (byProcessEventId.isEmpty()) {
            notificationService.process(record);
            ProcessEventModel processEventModel = ProcessEventModel.builder()
                    .processEventId(UUID.fromString(record.key()))
                    .build();
            processEventRepository.save(processEventModel);
        }
    }
}
