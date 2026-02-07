package com.ticktickdoc.service;

import com.ticktickdoc.domain.LoginDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.notification.enums.NotificationTypeEnum;
import com.ticktickdoc.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private UserDomain userDomain;

    @BeforeEach
    void setUp() {
        userDomain = UserDomain.builder()
                .id(1L)
                .fullName("Test User")
                .email("test@mail.com")
                .password("encodedPassword")
                .notificationType(Set.of(NotificationTypeEnum.EMAIL))
                .telegram("tg")
                .build();
    }

    @Test
    void register() {
        when(jwtUtil.generateToken(any(Long.class))).thenReturn("testToken");
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userService.createUser(any(UserDomain.class))).thenReturn(userDomain);
        String register = authenticationService.register(userDomain);
        assertEquals("testToken", register);
    }

    @Test
    void login() {
        LoginDomain loginDomain = LoginDomain.builder()
                .email("test@mail.com")
                .password("encodedPassword")
                .build();
        when(userService.findUserByEmail(any(String.class))).thenReturn(java.util.Optional.of(userDomain));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(jwtUtil.generateToken(any(Long.class))).thenReturn("testToken");
        String login = authenticationService.login(loginDomain);
        assertEquals("testToken", login);
    }
}