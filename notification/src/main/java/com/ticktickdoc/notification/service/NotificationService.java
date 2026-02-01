package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface NotificationService {

    void process(ConsumerRecord<String, NotificationDomain> record);

}
