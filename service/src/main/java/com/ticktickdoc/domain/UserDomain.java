package com.ticktickdoc.domain;

import com.ticktickdoc.notification.enums.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDomain {

    private Long id;
    private String fullName;
    private String telegram;
    private Set<NotificationTypeEnum> notificationType;
    private String email;
    private String password;
}
