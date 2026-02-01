package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;

public interface NotificationSender {

    void send(String userEmail, NotificationDomain domain);
}
