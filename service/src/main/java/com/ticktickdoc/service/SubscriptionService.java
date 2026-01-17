package com.ticktickdoc.service;

import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.feignClient.domain.PaymentDomain;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDomain createSubscription(Long userId);

    List<SubscriptionDomain> getSubscriptionByUserId(Long userId);

    Boolean verifySubscriptionByUserId(Long userId);

    void deleteAllSubscriptionsByUserId(Long userId);

    PaymentDomain payments();
}
