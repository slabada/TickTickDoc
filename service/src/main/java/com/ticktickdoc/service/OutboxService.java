package com.ticktickdoc.service;

import com.ticktickdoc.model.entity.OutboxModel;

public interface OutboxService {

    OutboxModel save(OutboxModel outbox);

    void sendNotifications();
}
