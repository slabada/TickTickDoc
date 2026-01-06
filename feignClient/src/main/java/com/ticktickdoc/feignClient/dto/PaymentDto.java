package com.ticktickdoc.feignClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String id;

    private AmountDto amount;

    @JsonProperty("payment_method_data")
    private PaymentMethodDataDto paymentMethodData;

    private ConfirmationDto  confirmation;

    private Boolean capture;

    @JsonProperty("metadata")
    private Map<Object, Object> metaData;

    private String description;

    private ReceiptDto receipt;
}
