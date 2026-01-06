package com.ticktickdoc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
public class SubscriptionDto {
    private Long id;
    private UserDto user;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
