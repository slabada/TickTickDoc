package com.ticktickdoc.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {
    EMAIL,
    TELEGRAM;

    public static final String EMAIL_VALUE = "EMAIL";
    public static final String TELEGRAM_VALUE = "TELEGRAM";
}
