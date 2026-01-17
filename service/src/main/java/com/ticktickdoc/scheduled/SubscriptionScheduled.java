package com.ticktickdoc.scheduled;

import com.ticktickdoc.model.entity.SubscriptionModel;
import com.ticktickdoc.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduled {

    private final SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void deactivateSubscription() {
        List<SubscriptionModel> subscriptions = subscriptionRepository.findAllByEndDate(LocalDate.now()).stream()
                .peek(s -> s.setIsActive(Boolean.FALSE))
                .toList();
        subscriptionRepository.saveAll(subscriptions);
    }
}
