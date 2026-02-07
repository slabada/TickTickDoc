package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.InviteActionDomain;
import com.ticktickdoc.domain.InviteRequestDomain;
import com.ticktickdoc.domain.MessageNotificationDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.domain.invent.MessageNotificationInvent;
import com.ticktickdoc.enums.InventStatusEnum;
import com.ticktickdoc.model.entity.UserInventModel;
import com.ticktickdoc.repository.UserInventRepository;
import com.ticktickdoc.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageNotificationServiceImplTest {

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private UserService userService;

    @Mock
    private UserInventRepository userInventRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private MessageNotificationServiceImpl service;

    private UserAdaptor currentUser;
    private UserDomain targetUser;

    @BeforeEach
    void setUp() {
        var user = UserDomain.builder()
                .id(1L)
                .fullName("Current User")
                .build();

        currentUser = UserAdaptor.builder()
                .user(user)
                .build();

        targetUser = UserDomain.builder()
                .id(2L)
                .email("test@gmail.com")
                .telegram("tg")
                .build();
    }

    @Test
    void invites() {
        InviteRequestDomain request = new InviteRequestDomain("target@mail.com");

        when(securityUtil.getUserSecurity()).thenReturn(currentUser);
        when(userService.findUserByEmail("target@mail.com"))
                .thenReturn(Optional.of(targetUser));

        service.invites(request);

        verify(messagingTemplate).convertAndSendToUser(
                eq("2"),
                eq("/queue/notifications"),
                any(MessageNotificationDomain.class)
        );

        verify(userInventRepository).save(any(UserInventModel.class));
    }

    @Test
    void action_ACCEPTED() {
        UserInventModel invent = new UserInventModel();
        invent.setInventId("123");
        invent.setLinkFromUserId(1L);

        InviteActionDomain action = new InviteActionDomain("123", InventStatusEnum.ACCEPTED);

        when(userInventRepository.findByInventId("123"))
                .thenReturn(Optional.of(invent));

        service.action(action);

        verify(userInventRepository).save(invent);
        verify(publisher).publishEvent(any(MessageNotificationInvent.class));
    }

    @Test
    void action_DECLINED() {
        UserInventModel invent = new UserInventModel();
        invent.setInventId("123");

        InviteActionDomain action = new InviteActionDomain("123", InventStatusEnum.DECLINED);

        when(userInventRepository.findByInventId("123"))
                .thenReturn(Optional.of(invent));

        service.action(action);

        verify(userInventRepository).save(invent);
        verify(publisher, never()).publishEvent(any());
    }
}