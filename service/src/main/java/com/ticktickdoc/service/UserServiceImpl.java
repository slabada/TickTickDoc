package com.ticktickdoc.service;

import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.exception.UserException;
import com.ticktickdoc.mapper.UserMapper;
import com.ticktickdoc.model.entity.UserModel;
import com.ticktickdoc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDomain getUser(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(UserException.NullUserException::new);
        return userMapper.toDomain(user);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionDomain createSubscriptionFroUser(Long userId) {
        return subscriptionService.createSubscription(userId);
    }

    @Override
    @Transactional
    public UserDomain createUser(UserDomain user) {
        Optional<UserModel> userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new UserException.ConflictRegistrationUserException();
        }
        UserModel model = userMapper.toModel(user);
        UserModel save = userRepository.save(model);
        return userMapper.toDomain(save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDomain> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    @Transactional
    public UserDomain updateUser(Long id, UserDomain user) {
        UserModel userById = userRepository.findById(id)
                .orElseThrow(UserException.NullUserException::new);
        userMapper.updateUser(user, userById);
        UserModel save = userRepository.save(userById);
        return userMapper.toDomain(save);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            subscriptionService.deleteAllSubscriptionsByUserId(id);
            userRepository.deleteById(id);
        }
    }
}
