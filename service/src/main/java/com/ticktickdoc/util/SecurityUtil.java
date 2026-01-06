package com.ticktickdoc.util;

import com.ticktickdoc.adaptor.UserAdaptor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public UserAdaptor getUserSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Пользователь не аутентифицирован");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserAdaptor)) {
            throw new AuthenticationServiceException(
                    "Неподдерживаемый тип principal: " + principal.getClass().getName()
            );
        }
        return (UserAdaptor) principal;
    }
}
