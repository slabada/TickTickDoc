package com.ticktickdoc.model.entity;

import com.ticktickdoc.enums.InventStatusEnum;
import com.ticktickdoc.enums.InventTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserInventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String inventId;
    private Long linkFromUserId;
    private Long linkToUserId;
    @Enumerated(EnumType.STRING)
    private InventTypeEnum inventType;
    @Enumerated(EnumType.STRING)
    private InventStatusEnum inventStatus;
    private LocalDateTime dateDispatch;
    private LocalDateTime dateResponse;
}
