package com.ticktickdoc.controller;

import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.dto.SubscriptionDto;
import com.ticktickdoc.dto.UserDto;
import com.ticktickdoc.dto.UserUpdateDto;
import com.ticktickdoc.mapper.SubscriptionMapper;
import com.ticktickdoc.mapper.UserMapper;
import com.ticktickdoc.service.UserService;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final SecurityUtil securityUtil;

    private final UserService userService;
    private final UserMapper userMapper;
    private final SubscriptionMapper subscriptionMapper;

    @GetMapping("/user/me")
    public ResponseEntity<UserDto> getMyUser() {
        Long id = securityUtil.getUserSecurity().getId();
        UserDomain user = userService.getUser(id);
        UserDto dto = userMapper.toDto(user);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        UserDomain user = userService.getUser(id);
        UserDto dto = userMapper.toDto(user);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateDto user) {
        UserDomain domain = userMapper.toDomain(user);
        UserDomain userDomain = userService.updateUser(id, domain);
        UserDto dto = userMapper.toDto(userDomain);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/user/subscription/{id}")
    public ResponseEntity<SubscriptionDto> createSubscriptionFroUser(@PathVariable("id") Long id) {
        SubscriptionDomain subscriptionFroUser = userService.createSubscriptionFroUser(id);
        SubscriptionDto dto = subscriptionMapper.toDto(subscriptionFroUser);
        return ResponseEntity.ok().body(dto);
    }
}
