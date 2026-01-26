package com.bizcore.dto;

import java.util.List;

public class UpdateMenuItemRequest {

    private String title;
    private String icon;
    private String path;
    private Integer displayOrder;
    private Boolean active;
    private Boolean visible;
    private String requiredPermission;
    private List<Long> allowedRoleIds;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public String getRequiredPermission() { return requiredPermission; }
    public void setRequiredPermission(String requiredPermission) { this.requiredPermission = requiredPermission; }

    public List<Long> getAllowedRoleIds() { return allowedRoleIds; }
    public void setAllowedRoleIds(List<Long> allowedRoleIds) { this.allowedRoleIds = allowedRoleIds; }
}
