package com.bizcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateMenuItemRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    private Long parentId;

    @NotBlank(message = "Menu code is required")
    private String code;

    @NotBlank(message = "Menu title is required")
    private String title;

    private String icon;
    private String path;
    private Integer displayOrder;
    private String requiredPermission;
    private List<Long> allowedRoleIds;

    // Getters and Setters
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public String getRequiredPermission() { return requiredPermission; }
    public void setRequiredPermission(String requiredPermission) { this.requiredPermission = requiredPermission; }

    public List<Long> getAllowedRoleIds() { return allowedRoleIds; }
    public void setAllowedRoleIds(List<Long> allowedRoleIds) { this.allowedRoleIds = allowedRoleIds; }
}
