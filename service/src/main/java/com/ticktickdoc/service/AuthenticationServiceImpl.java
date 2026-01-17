package com.ticktickdoc.service;

import com.ticktickdoc.domain.LoginDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.enums.NotificationTypeEnum;
import com.ticktickdoc.exception.AuthenticationException;
import com.ticktickdoc.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Override
    @Transactional
    public String register(UserDomain reg) {
        String hashPassword = passwordEncoder.encode(reg.getPassword());
        UserDomain userDomain = new UserDomain().toBuilder()
                .fullName(reg.getFullName())
                .email(reg.getEmail())
                .notificationType(Set.of(NotificationTypeEnum.EMAIL))
                .password(hashPassword)
                .telegram(reg.getTelegram())
                .build();
        UserDomain user = userService.createUser(userDomain);
        return jwtUtil.generateToken(user.getId());
    }

    @Override
    @Transactional
    public String login(LoginDomain login) {
        UserDomain user = userService.findUserByEmail(login.getEmail())
                .orElseThrow(AuthenticationException.ConflictAuthException::new);
        if (!user.getEmail().equals(login.getEmail())) {
            throw new AuthenticationException.ConflictAuthException();
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new AuthenticationException.ConflictAuthException();
        }
        return jwtUtil.generateToken(user.getId());
    }
}
