package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.domain.invent.MessageNotificationInvent;
import com.ticktickdoc.model.entity.UserChildModel;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import com.ticktickdoc.repository.UserChildRepository;
import com.ticktickdoc.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserChildServiceImplTest {

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private UserChildRepository userChildRepository;

    @InjectMocks
    private UserChildServiceImpl service;

    private UserAdaptor userAdaptor;

    @BeforeEach
    void setUp() {
        var userDomain = UserDomain.builder()
                .id(100L)
                .build();

        userAdaptor = UserAdaptor.builder()
                .user(userDomain)
                .build();
    }

    @Test
    void addUserChild() {
        MessageNotificationInvent invent = new MessageNotificationInvent(200L);

        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(userChildRepository.existsByParentUserId(200L)).thenReturn(false);
        when(userChildRepository.existsByChildUserIdAndParentUserId(200L, 100L)).thenReturn(false);
        when(userChildRepository.existsByChildUserId(200L)).thenReturn(false);

        service.addUserChild(invent);

        verify(userChildRepository).save(any(UserChildModel.class));
    }

    @Test
    void deleteUserChild() {
        RequestIdDomain request = new RequestIdDomain(200L);

        UserChildModel model = UserChildModel.builder()
                .parentUserId(100L)
                .childUserId(200L)
                .build();

        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(userChildRepository.removeByParentUserIdAndChildUserId(100L, 200L))
                .thenReturn(model);

        ResponseIdDomain response = service.deleteUserChild(request);

        assertEquals(200L, response.getId());
    }

    @Test
    void getUserChildDocument() {
        UserChildDocumentProjection projection = mock(UserChildDocumentProjection.class);

        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(userChildRepository.findUserWithDocumentCount(100L))
                .thenReturn(List.of(projection));

        List<UserChildDocumentProjection> result = service.getUserChildDocument();

        assertEquals(1, result.size());
        verify(userChildRepository).findUserWithDocumentCount(100L);
    }

    @Test
    void findAllChildUserByUserId() {
        when(userChildRepository.findAllByParentUserId(100L))
                .thenReturn(List.of(200L, 300L));

        List<Long> result = service.findAllChildUserByUserId(100L);

        assertEquals(2, result.size());
        assertEquals(200L, result.getFirst());
    }
}