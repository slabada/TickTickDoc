package com.ticktickdoc.service;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.exception.UserChildException;
import com.ticktickdoc.model.entity.UserChildModel;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import com.ticktickdoc.repository.UserChildRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChildServiceImpl implements UserChildService {

    private final SecurityUtil securityUtil;

    private final UserChildRepository userChildRepository;

    @Override
    @Transactional
    public ResponseIdDomain addUserChild(RequestIdDomain request) {
        Long currentId = securityUtil.getUserSecurity().getId();
        if (request.getId().equals(currentId)) {
            throw new UserChildException.ConflictAddChildCurrentUserException();
        }
        boolean existsParent = userChildRepository.existsByParentUserId(request.getId());
        if (existsParent) {
            throw new UserChildException.ConflictAddChildUserException();
        }
        boolean existsChildAndParent = userChildRepository.existsByChildUserIdAndParentUserId(request.getId(), currentId);
        if (existsChildAndParent) {
            throw new UserChildException.ConflictAddChildDuplicateUserException();
        }
        boolean existsChild = userChildRepository.existsByChildUserId(request.getId());
        if (existsChild) {
            throw new UserChildException.UserAlreadyChildOfAnotherUserException();
        }
        UserChildModel userChild = UserChildModel.builder()
                .parentUserId(currentId)
                .childUserId(request.getId())
                .build();
        UserChildModel save = userChildRepository.save(userChild);
        return new ResponseIdDomain(save.getChildUserId());
    }

    @Override
    @Transactional
    public ResponseIdDomain deleteUserChild(RequestIdDomain request) {
        Long currentId = securityUtil.getUserSecurity().getId();
        UserChildModel delete = userChildRepository.removeByParentUserIdAndChildUserId(currentId, request.getId());
        return new ResponseIdDomain(delete.getChildUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserChildDocumentProjection> getUserChildDocument() {
        Long currentUser = securityUtil.getUserSecurity().getId();
        return userChildRepository.findUserWithDocumentCount(currentUser);
    }
}
