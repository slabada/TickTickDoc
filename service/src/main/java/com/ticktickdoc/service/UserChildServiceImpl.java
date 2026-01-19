package com.ticktickdoc.service;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.domain.invent.MessageNotificationInvent;
import com.ticktickdoc.exception.UserChildException;
import com.ticktickdoc.model.entity.UserChildModel;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import com.ticktickdoc.repository.UserChildRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChildServiceImpl implements UserChildService {

    private final SecurityUtil securityUtil;
    private final UserChildRepository userChildRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void addUserChild(MessageNotificationInvent invent) {
        Long currentId = securityUtil.getUserSecurity().getId();
        if (invent.getId().equals(currentId)) {
            throw new UserChildException.ConflictAddChildCurrentUserException();
        }
        boolean existsParent = userChildRepository.existsByParentUserId(invent.getId());
        if (existsParent) {
            throw new UserChildException.ConflictAddChildUserException();
        }
        boolean existsChildAndParent = userChildRepository.existsByChildUserIdAndParentUserId(invent.getId(), currentId);
        if (existsChildAndParent) {
            throw new UserChildException.ConflictAddChildDuplicateUserException();
        }
        boolean existsChild = userChildRepository.existsByChildUserId(invent.getId());
        if (existsChild) {
            throw new UserChildException.UserAlreadyChildOfAnotherUserException();
        }
        UserChildModel userChild = UserChildModel.builder()
                .parentUserId(invent.getId())
                .childUserId(currentId)
                .build();
        userChildRepository.save(userChild);
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
