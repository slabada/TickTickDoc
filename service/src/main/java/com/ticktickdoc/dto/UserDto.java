package com.ticktickdoc.dto;

import com.ticktickdoc.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {

    private Long id;
    private String nickname;
    private String description;
    private String email;
    private LocalDateTime registrationDate;
    private Set<UserDto> linkSubsidiaryUser;
}
