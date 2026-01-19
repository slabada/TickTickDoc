package com.ticktickdoc.service;

import com.ticktickdoc.domain.InviteActionDomain;
import com.ticktickdoc.domain.InviteRequestDomain;

public interface MessageNotificationService {

    void invites(InviteRequestDomain request);

    void action(InviteActionDomain action);
}
