package com.ticktickdoc.controller;

import com.ticktickdoc.domain.PaymentDomain;
import com.ticktickdoc.feignClient.client.YooKassaClient;
import com.ticktickdoc.feignClient.dto.PaymentDto;
import com.ticktickdoc.mapper.SubscriptionMapper;
import com.ticktickdoc.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final YooKassaClient yooKassaClient;
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @PostMapping("/payments")
    public ResponseEntity<PaymentDto> payments() {
        PaymentDomain payment = subscriptionService.payments();
        PaymentDto paymentDto = subscriptionMapper.toDto(payment);
        PaymentDto dto = yooKassaClient.createPayment(UUID.randomUUID().toString(), paymentDto);
        return ResponseEntity.ok().body(dto);
    }
}
