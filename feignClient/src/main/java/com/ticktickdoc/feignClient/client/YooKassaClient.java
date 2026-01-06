package com.ticktickdoc.feignClient.client;

import com.ticktickdoc.feignClient.config.FeignConfig;
import com.ticktickdoc.feignClient.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "yooKassa", url = "https://api.yookassa.ru/v3", configuration = FeignConfig.class)
public interface YooKassaClient {

    @PostMapping(value = "/payments", consumes = "application/json")
    PaymentDto createPayment(
            @RequestHeader("Idempotence-Key") String idempotenceKey,
            @RequestBody PaymentDto payment
    );

    @GetMapping(value = "/payments/{paymentId}")
    PaymentDto getPaymentById(@PathVariable("paymentId") String paymentId);
}
