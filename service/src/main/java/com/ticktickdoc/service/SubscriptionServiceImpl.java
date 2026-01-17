package com.ticktickdoc.service;

import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.feignClient.domain.AmountDomain;
import com.ticktickdoc.feignClient.domain.ConfirmationDomain;
import com.ticktickdoc.feignClient.domain.CustomerDomain;
import com.ticktickdoc.feignClient.domain.ItemsDomain;
import com.ticktickdoc.feignClient.domain.PaymentDomain;
import com.ticktickdoc.feignClient.domain.PaymentMethodDataDomain;
import com.ticktickdoc.feignClient.domain.ReceiptDomain;
import com.ticktickdoc.feignClient.enums.CurrencyEnum;
import com.ticktickdoc.feignClient.enums.TypeConfirmation;
import com.ticktickdoc.feignClient.enums.TypePaymentMethod;
import com.ticktickdoc.mapper.SubscriptionMapper;
import com.ticktickdoc.model.entity.SubscriptionModel;
import com.ticktickdoc.repository.SubscriptionRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityUtil securityUtil;

    @Value("${payment.description}")
    private String paymentDescription;

    @Value("${item.description}")
    private String itemDescription;

    @Value("${configuration.return.url}")
    private String returnUrl;

    @Value("${amount.value}")
    private String amountValue;

    @Override
    @Transactional
    public SubscriptionDomain createSubscription(Long userId) {
        SubscriptionModel subscription = SubscriptionModel.builder()
                .linkUser(userId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .isActive(true)
                .build();
        SubscriptionModel save = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDomain(save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDomain> getSubscriptionByUserId(Long userId) {
        List<SubscriptionModel> subscription = subscriptionRepository.findAllByLinkUser(userId);
        return subscriptionMapper.toDomain(subscription);
    }

    @Override
    @Transactional
    public Boolean verifySubscriptionByUserId(Long userId) {
        List<SubscriptionModel> subscription = subscriptionRepository.findAllByLinkUser(userId);
        return subscription.stream()
                .anyMatch(SubscriptionModel::getIsActive);
    }

    @Override
    @Transactional
    public void deleteAllSubscriptionsByUserId(Long userId) {
        List<SubscriptionDomain> subscriptions = getSubscriptionByUserId(userId);
        List<SubscriptionModel> subscriptionModels = subscriptions.stream()
                .map(subscriptionMapper::toModel)
                .toList();
        subscriptionRepository.deleteAll(subscriptionModels);
    }

    @Override
    @Transactional
    public PaymentDomain payments() {
        Long id = securityUtil.getUserSecurity().getId();
        String email = securityUtil.getUserSecurity().getEmail();
        AmountDomain amount = getAmountDomain();
        PaymentMethodDataDomain paymentMethodData = getPaymentMethodDataDomain();
        ConfirmationDomain confirmation = getConfirmationDomain();
        ItemsDomain items = getItemsDomain(amount);
        CustomerDomain customer = getCustomerDomain(email);
        ReceiptDomain receipt = getReceiptDomain(customer, items);

        Map<Object, Object> metaData = new HashMap<>();
        metaData.put("userId", id);

        PaymentDomain payment = new PaymentDomain();
        payment.setAmount(amount);
        payment.setPaymentMethodData(paymentMethodData);
        payment.setConfirmation(confirmation);
        payment.setCapture(true);
        payment.setMetaData(metaData);
        payment.setDescription(paymentDescription);
        payment.setReceipt(receipt);
        return payment;
    }

    private ReceiptDomain getReceiptDomain(CustomerDomain customer, ItemsDomain items) {
        ReceiptDomain receipt = new ReceiptDomain();
        receipt.setCustomer(customer);
        receipt.setItems(List.of(items));
        return receipt;
    }

    private CustomerDomain getCustomerDomain(String email) {
        CustomerDomain customer = new CustomerDomain();
        customer.setEmail(email);
        return customer;
    }

    private ItemsDomain getItemsDomain(AmountDomain amount) {
        ItemsDomain items = new ItemsDomain();
        items.setDescription(itemDescription);
        items.setAmount(amount);
        items.setVatCode(12);
        items.setQuantity(1);
        return items;
    }

    private ConfirmationDomain getConfirmationDomain() {
        ConfirmationDomain confirmation = new ConfirmationDomain();
        confirmation.setType(TypeConfirmation.REDIRECT.getValue());
        confirmation.setReturnUrl(returnUrl);
        return confirmation;
    }

    private PaymentMethodDataDomain getPaymentMethodDataDomain() {
        PaymentMethodDataDomain paymentMethodData = new PaymentMethodDataDomain();
        paymentMethodData.setType(TypePaymentMethod.BANK_CARD.getValue());
        return paymentMethodData;
    }

    private AmountDomain getAmountDomain() {
        AmountDomain amount = new AmountDomain();
        amount.setValue(amountValue);
        amount.setCurrency(CurrencyEnum.RUB);
        return amount;
    }
}
