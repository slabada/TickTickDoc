package com.ticktickdoc.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
public class SubscriptionDomain {
    private Long id;
    private UserDomain user;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
