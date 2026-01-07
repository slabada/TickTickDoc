package com.ticktickdoc.service;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.domain.SubscriptionDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.exception.UserException;
import com.ticktickdoc.mapper.UserMapper;
import com.ticktickdoc.model.UserModel;
import com.ticktickdoc.repository.UserRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final SecurityUtil securityUtil;

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
        var user = userRepository.findById(userId)
                .orElseThrow(UserException.NullUserException::new);
        return subscriptionService.createSubscription(user);
    }

    @Override
    @Transactional
    public UserDomain createUser(UserDomain user) {
        Optional<UserModel> userByEmail = userRepository.findByEmail(user.getEmail());
        if(userByEmail.isPresent()) {
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
        if(userRepository.existsById(id)) {
            subscriptionService.deleteAllSubscriptionsByUserId(id);
            userRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public List<ResponseIdDomain> addSubsidiaryUser(List<RequestIdDomain> ids) {
        Long currentId = securityUtil.getUserSecurity().getId();
        UserDomain currentUser = getUser(currentId);
        Set<UserDomain> childs = new HashSet<>();
        List<ResponseIdDomain> response = new ArrayList<>();
        ids.forEach(u -> {
            UserDomain user = getUser(u.getId());
            boolean checkDublicate = currentUser.getLinkSubsidiaryUser().stream()
                    .map(UserDomain::getId)
                    .anyMatch(id -> id.equals(u.getId()));
            if(checkDublicate) {
                throw new UserException.ConflictAddChildDuplicateUserException();
            }
            if(!user.getLinkSubsidiaryUser().isEmpty()){
                throw new UserException.ConflictAddChildUserException();
            }
            if(currentId.equals(user.getId())) {
                throw new UserException.ConflictAddChildCurrentUserException();
            }
            childs.add(user);
            response.add(new ResponseIdDomain(u.getId()));
        });
        currentUser.setLinkSubsidiaryUser(childs);
        UserModel model = userMapper.toModel(currentUser);
        userRepository.save(model);
        return response;
    }

    @Override
    @Transactional
    public List<ResponseIdDomain> deleteSubsidiaryUser(List<RequestIdDomain> ids) {
        Long currentId = securityUtil.getUserSecurity().getId();
        UserDomain currentUser = getUser(currentId);
        Set<Long> idsToRemove = ids.stream()
                .map(RequestIdDomain::getId)
                .collect(Collectors.toSet());
        List<ResponseIdDomain> removed = currentUser.getLinkSubsidiaryUser().stream()
                .filter(u -> idsToRemove.contains(u.getId()))
                .map(u -> new ResponseIdDomain(u.getId()))
                .toList();
        currentUser.getLinkSubsidiaryUser()
                .removeIf(u -> idsToRemove.contains(u.getId()));
        UserModel model = userMapper.toModel(currentUser);
        userRepository.save(model);
        return removed;
    }
}
