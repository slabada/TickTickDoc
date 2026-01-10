package com.ticktickdoc.notification.service;

import com.ticktickdoc.notification.domain.NotificationDomain;

public interface Notification {

    void send(NotificationDomain domain);
}
