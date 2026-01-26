package com.bizcore.security;

import java.util.List;

public class UserPrincipal {

    private Long userId;
    private Long companyId;
    private String companyCode;
    private String username;
    private List<String> roles;
    private List<String> permissions;

    public UserPrincipal(Long userId, Long companyId, String companyCode,
                         String username, List<String> roles, List<String> permissions) {
        this.userId = userId;
        this.companyId = companyId;
        this.companyCode = companyCode;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
    }

    public Long getUserId() { return userId; }
    public Long getCompanyId() { return companyId; }
    public String getCompanyCode() { return companyCode; }
    public String getUsername() { return username; }
    public List<String> getRoles() { return roles; }
    public List<String> getPermissions() { return permissions; }

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    public boolean hasAnyRole(String... checkRoles) {
        if (roles == null) return false;
        for (String role : checkRoles) {
            if (roles.contains(role)) return true;
        }
        return false;
    }

    public boolean hasAnyPermission(String... checkPermissions) {
        if (permissions == null) return false;
        for (String permission : checkPermissions) {
            if (permissions.contains(permission)) return true;
        }
        return false;
    }
}
