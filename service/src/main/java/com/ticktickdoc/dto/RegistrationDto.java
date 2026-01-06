package com.ticktickdoc.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationDto {

    private String nickname;
    private String email;
    private String password;
    private String description;
}
