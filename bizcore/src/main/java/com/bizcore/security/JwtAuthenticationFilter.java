package com.bizcore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.startsWith("/api/public/") || path.startsWith("/actuator/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserId(token);
            String username = tokenProvider.getUsername(token);
            List<String> roles = tokenProvider.getRoles(token);
            List<String> permissions = tokenProvider.getPermissions(token);

            // Add both roles (with ROLE_ prefix) and permissions as authorities
            // Handle null-safety for roles and permissions
            List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();

            if (roles != null && !roles.isEmpty()) {
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }

            if (permissions != null && !permissions.isEmpty()) {
                permissions.forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));
            }

            // If user has no authorities, deny authentication
            if (authorities.isEmpty()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User has no roles or permissions assigned");
                return;
            }

            UserPrincipal principal = new UserPrincipal(
                userId,
                tokenProvider.getCompanyId(token),
                tokenProvider.getCompanyCode(token),
                username,
                roles != null ? roles : List.of(),
                permissions != null ? permissions : List.of()
            );

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
