package com.ticktickdoc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDomain {

    private Long id;
    private String nickname;
    private String description;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private List<UserDomain> linkSubsidiaryUser;
}
