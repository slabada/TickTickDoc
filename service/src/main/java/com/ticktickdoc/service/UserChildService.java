package com.ticktickdoc.service;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;

import java.util.List;

public interface UserChildService {

    ResponseIdDomain addUserChild(RequestIdDomain request);

    ResponseIdDomain deleteUserChild(RequestIdDomain request);

    List<UserChildDocumentProjection> getUserChildDocument();
}
