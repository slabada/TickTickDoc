package com.ticktickdoc.domain;

import com.ticktickdoc.enums.InventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MessageNotificationDomain {

    private InventTypeEnum invent;
    private String fullName;
    private String from;
    private LocalDateTime dateDispatch;
    private String inviteId;
}
