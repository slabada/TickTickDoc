package com.ticktickdoc.repository;

import com.ticktickdoc.model.entity.SubscriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionModel, Long> {

    List<SubscriptionModel> findAllByLinkUser(Long userId);

    List<SubscriptionModel> findAllByEndDate(LocalDate endDate);
}
