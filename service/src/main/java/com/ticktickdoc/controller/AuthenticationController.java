package com.ticktickdoc.controller;

import com.ticktickdoc.domain.LoginDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.dto.JwtDto;
import com.ticktickdoc.dto.LoginDto;
import com.ticktickdoc.dto.RegistrationDto;
import com.ticktickdoc.mapper.UserMapper;
import com.ticktickdoc.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    public ResponseEntity<JwtDto> registration(@RequestBody RegistrationDto registration) {
        UserDomain domain = userMapper.toDomain(registration);
        String jwt = authenticationService.register(domain);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JwtDto(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginDto login) {
        LoginDomain loginDomain = new LoginDomain(
                login.getEmail(),
                login.getPassword()
        );
        String jwt = authenticationService.login(loginDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtDto(jwt));
    }
}
