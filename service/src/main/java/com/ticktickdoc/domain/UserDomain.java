package com.ticktickdoc.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<UserDomain> linkSubsidiaryUser;
}
