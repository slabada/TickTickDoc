package com.ticktickdoc.service;

import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.domain.UserDomain;

import java.util.Optional;

public interface UserService {

    UserDomain getUser(Long userId);

    SubscriptionDomain createSubscriptionFroUser(Long userId);

    UserDomain createUser(UserDomain user);

    Optional<UserDomain> findUserByEmail(String email);

    UserDomain updateUser(Long id, UserDomain user);

    void deleteUser(Long id);
}
