package com.ticktickdoc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
