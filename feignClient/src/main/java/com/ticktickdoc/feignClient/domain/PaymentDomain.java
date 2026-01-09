package com.ticktickdoc.feignClient.domain;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaymentDomain {

    private String id;

    private AmountDomain amount;

    private PaymentMethodDataDomain paymentMethodData;

    private ConfirmationDomain confirmation;

    private Boolean capture;

    private Map<Object, Object> metaData;

    private String description;

    private ReceiptDomain receipt;
}
