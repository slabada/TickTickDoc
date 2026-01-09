package com.ticktickdoc.service;

import com.ticktickdoc.feignClient.domain.PaymentDomain;
import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.model.UserModel;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDomain createSubscription(UserModel user);

    List<SubscriptionDomain> getSubscriptionByUserId(Long userId);

    Boolean verifySubscriptionByUserId(Long userId);

    void deleteAllSubscriptionsByUserId(Long userId);

    PaymentDomain payments();
}
