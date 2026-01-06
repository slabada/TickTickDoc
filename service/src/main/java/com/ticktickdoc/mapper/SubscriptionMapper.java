package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.PaymentDomain;
import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.dto.SubscriptionDto;
import com.ticktickdoc.feignClient.dto.PaymentDto;
import com.ticktickdoc.model.SubscriptionModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SubscriptionMapper {

    SubscriptionDomain toDomain(SubscriptionModel subscriptionModel);

    List<SubscriptionDomain> toDomain(List<SubscriptionModel> subscriptionModel);

    SubscriptionModel toModel(SubscriptionDomain subscriptionDomain);

    SubscriptionDto toDto(SubscriptionDomain subscriptionDomain);

    PaymentDto toDto(PaymentDomain domain);
}
