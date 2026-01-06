package com.ticktickdoc.adaptor;

import com.ticktickdoc.domain.UserDomain;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder(toBuilder = true)
public class UserAdaptor implements UserDetails {

    private UserDomain user;

    private Collection<? extends GrantedAuthority> authorities;

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
