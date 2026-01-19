package com.ticktickdoc.controller;

import com.ticktickdoc.domain.InviteActionDomain;
import com.ticktickdoc.domain.InviteRequestDomain;
import com.ticktickdoc.dto.InviteActionDto;
import com.ticktickdoc.dto.InviteRequestDto;
import com.ticktickdoc.mapper.MessageNotificationMapper;
import com.ticktickdoc.service.MessageNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageNotificationController {

    private final MessageNotificationService messageNotificationService;
    private final MessageNotificationMapper messageNotificationMapper;

    @PostMapping("/invites")
    public ResponseEntity<Void> invites(@RequestBody InviteRequestDto request) {
        InviteRequestDomain domain = messageNotificationMapper.toDomain(request);
        messageNotificationService.invites(domain);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invites/action")
    public ResponseEntity<Void> action(@RequestBody InviteActionDto action) {
        InviteActionDomain domain = messageNotificationMapper.toDomain(action);
        messageNotificationService.action(domain);
        return ResponseEntity.ok().build();
    }
}
