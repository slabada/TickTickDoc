package com.ticktickdoc.util;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.enums.SubscriptionEnum;
import com.ticktickdoc.service.SubscriptionService;
import com.ticktickdoc.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserService userService;
    private final SubscriptionService  subscriptionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        Long userId = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                userId = jwtUtil.getUserId(token);
            }
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDomain user = userService.getUser(userId);
            Boolean verifiedSubscription = subscriptionService.verifySubscriptionByUserId(user.getId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(verifiedSubscription){
                authorities.add(new SimpleGrantedAuthority(SubscriptionEnum.SUBSCRIPTION_ACTIVE.getValue()));
            }
            UserDetails userDetails = UserAdaptor.builder()
                    .user(user)
                    .authorities(authorities)
                    .build();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
