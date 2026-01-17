package com.ticktickdoc.controller;

import com.ticktickdoc.dto.WebhookDto;
import com.ticktickdoc.dto.WebhookObjectDto;
import com.ticktickdoc.enums.PaymentStatusEnum;
import com.ticktickdoc.feignClient.client.YooKassaClient;
import com.ticktickdoc.feignClient.dto.PaymentDto;
import com.ticktickdoc.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WebhookController {

    private final YooKassaClient yooKassaClient;

    private final SubscriptionService subscriptionService;

    @PostMapping("/webhook")
    public void webhook(@RequestBody WebhookDto webhook) {
        WebhookObjectDto object = webhook.getObject();
        String paymentId = object.getId();
        PaymentDto payment = yooKassaClient.getPaymentById(paymentId);
        String paymentIdFroKassa = payment.getId();
        String userId = (String) payment.getMetaData().get("userId");
        if(object.getId().equals(paymentIdFroKassa) && object.getStatus().equals(PaymentStatusEnum.SUCCEEDED.getValue())) {
            createSubscription(Long.parseLong(userId));
        }
    }

    private void createSubscription(Long id) {
        subscriptionService.createSubscription(id);
    }
}
