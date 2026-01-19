package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.InviteActionDomain;
import com.ticktickdoc.domain.InviteRequestDomain;
import com.ticktickdoc.domain.MessageNotificationDomain;
import com.ticktickdoc.domain.invent.MessageNotificationInvent;
import com.ticktickdoc.enums.InventStatusEnum;
import com.ticktickdoc.enums.InventTypeEnum;
import com.ticktickdoc.model.entity.UserInventModel;
import com.ticktickdoc.repository.UserInventRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageNotificationServiceImpl implements MessageNotificationService {

    private final SecurityUtil securityUtil;

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final UserInventRepository userInventRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void invites(InviteRequestDomain request) {
        UserAdaptor currentUser = securityUtil.getUserSecurity();
        MessageNotificationDomain message = MessageNotificationDomain.builder()
                .invent(InventTypeEnum.ADD_CHILD_USER)
                .fullName(currentUser.getFullName())
                .from(currentUser.getEmail())
                .dateDispatch(LocalDateTime.now())
                .inviteId(String.valueOf(UUID.randomUUID()))
                .build();
        userService.findUserByEmail(request.getEmail())
                .ifPresent(u -> {
                    messagingTemplate.convertAndSendToUser(
                        String.valueOf(u.getId()),
                        "/queue/notifications",
                        message);
                    UserInventModel userInvent = UserInventModel.builder()
                            .inventId(message.getInviteId())
                            .linkFromUserId(currentUser.getId())
                            .linkToUserId(u.getId())
                            .inventType(InventTypeEnum.ADD_CHILD_USER)
                            .inventStatus(InventStatusEnum.PENDING)
                            .dateDispatch(LocalDateTime.now())
                            .build();
                    userInventRepository.save(userInvent);
                });

    }

    @Override
    @Transactional
    public void action(InviteActionDomain action) {
        userInventRepository.findByInventId(action.getActionId()).ifPresent(i -> {
            switch (action.getInventStatus()) {
                case ACCEPTED -> {
                    saveToUserInvent(i, action);
                    publisher.publishEvent(new MessageNotificationInvent(i.getLinkFromUserId()));
                }
                case DECLINED -> saveToUserInvent(i, action);
            }
        });
    }

    private void saveToUserInvent(UserInventModel i, InviteActionDomain action) {
        i.setDateResponse(LocalDateTime.now());
        i.setInventStatus(action.getInventStatus());
        userInventRepository.save(i);
    }
}
