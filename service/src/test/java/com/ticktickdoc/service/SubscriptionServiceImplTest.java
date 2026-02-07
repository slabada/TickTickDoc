package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.feignClient.domain.PaymentDomain;
import com.ticktickdoc.mapper.SubscriptionMapper;
import com.ticktickdoc.model.entity.SubscriptionModel;
import com.ticktickdoc.repository.SubscriptionRepository;
import com.ticktickdoc.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private SubscriptionServiceImpl service;

    private SubscriptionModel subscriptionModel;
    private SubscriptionDomain subscriptionDomain;
    private UserAdaptor userAdaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "paymentDescription", "test payment");
        ReflectionTestUtils.setField(service, "itemDescription", "test item");
        ReflectionTestUtils.setField(service, "returnUrl", "http://return");
        ReflectionTestUtils.setField(service, "amountValue", "100.00");

        var user = UserDomain.builder()
                .id(10L)
                .email("test@mail.com")
                .build();

        userAdaptor = UserAdaptor.builder()
                .user(user)
                .build();

        subscriptionModel = SubscriptionModel.builder()
                .id(1L)
                .linkUser(10L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .isActive(true)
                .build();

        subscriptionDomain = SubscriptionDomain.builder()
                .id(1L)
                .build();
    }

    @Test
    void createSubscription() {
        when(subscriptionRepository.save(any(SubscriptionModel.class)))
                .thenReturn(subscriptionModel);

        when(subscriptionMapper.toDomain(subscriptionModel))
                .thenReturn(subscriptionDomain);

        SubscriptionDomain result = service.createSubscription(10L);

        assertNotNull(result);
        verify(subscriptionRepository).save(any(SubscriptionModel.class));
    }

    @Test
    void getSubscriptionByUserId() {
        when(subscriptionRepository.findAllByLinkUser(10L))
                .thenReturn(List.of(subscriptionModel));

        when(subscriptionMapper.toDomain(List.of(subscriptionModel)))
                .thenReturn(List.of(subscriptionDomain));

        List<SubscriptionDomain> result = service.getSubscriptionByUserId(10L);

        assertEquals(1, result.size());
        verify(subscriptionRepository).findAllByLinkUser(10L);
    }

    @Test
    void verifySubscriptionByUserId() {
        when(subscriptionRepository.findAllByLinkUser(10L))
                .thenReturn(List.of(subscriptionModel));

        Boolean result = service.verifySubscriptionByUserId(10L);

        assertTrue(result);
    }

    @Test
    void deleteAllSubscriptionsByUserId() {
        when(subscriptionRepository.findAllByLinkUser(10L))
                .thenReturn(List.of(subscriptionModel));

        when(subscriptionMapper.toDomain(List.of(subscriptionModel)))
                .thenReturn(List.of(subscriptionDomain));

        when(subscriptionMapper.toModel(subscriptionDomain))
                .thenReturn(subscriptionModel);

        service.deleteAllSubscriptionsByUserId(10L);

        verify(subscriptionRepository).deleteAll(List.of(subscriptionModel));
    }

    @Test
    void payments() {
        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);

        PaymentDomain payment = service.payments();

        assertNotNull(payment);
        assertEquals("test payment", payment.getDescription());
        assertEquals("100.00", payment.getAmount().getValue());
        assertEquals("test@mail.com", payment.getReceipt().getCustomer().getEmail());
        assertTrue(payment.getCapture());
        assertEquals(10L, payment.getMetaData().get("userId"));
    }
}