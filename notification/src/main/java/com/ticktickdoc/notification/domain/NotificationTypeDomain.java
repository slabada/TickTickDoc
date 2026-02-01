package com.ticktickdoc.notification.domain;

import com.ticktickdoc.notification.enums.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationTypeDomain {
    
    private NotificationTypeEnum type;
    private String payload;
}
