package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.notification.domain.NotificationTypeDomain;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static com.ticktickdoc.notification.enums.NotificationTypeEnum.EMAIL_VALUE;
import static com.ticktickdoc.notification.enums.NotificationTypeEnum.TELEGRAM_VALUE;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final Map<String, NotificationSender> notificationSender;

    @Override
    public void process(ConsumerRecord<String, NotificationDomain> record) {
        Set<NotificationTypeDomain> notificationsType = record.value().getNotificationType();
        for (var notificationType : notificationsType) {
            switch (notificationType.getType()) {
                case EMAIL -> notificationSender.get(EMAIL_VALUE).send(
                        notificationType.getType().name(),
                        record.value()
                );
                case TELEGRAM -> notificationSender.get(TELEGRAM_VALUE).send(
                        notificationType.getType().name(),
                        record.value()
                );
            }
        }
    }
}
