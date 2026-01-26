package com.bizcore.dto;

import com.bizcore.entity.RequestLog;

import java.time.LocalDateTime;

public class RequestLogDTO {
    private Long id;
    private Long companyId;
    private Long userId;
    private String username;
    private String method;
    private String endpoint;
    private String queryParams;
    private String requestBody;
    private Integer responseStatus;
    private String responseBody;
    private String errorMessage;
    private String ipAddress;
    private String userAgent;
    private Long durationMs;
    private LocalDateTime createdAt;

    public static RequestLogDTO fromEntity(RequestLog entity) {
        RequestLogDTO dto = new RequestLogDTO();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompanyId());
        dto.setUserId(entity.getUserId());
        dto.setUsername(entity.getUsername());
        dto.setMethod(entity.getMethod());
        dto.setEndpoint(entity.getEndpoint());
        dto.setQueryParams(entity.getQueryParams());
        dto.setRequestBody(entity.getRequestBody());
        dto.setResponseStatus(entity.getResponseStatus());
        dto.setResponseBody(entity.getResponseBody());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setDurationMs(entity.getDurationMs());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
