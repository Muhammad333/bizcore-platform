package com.bizcore.dto;

import java.util.List;

public class AuthResponse {

    private boolean success;
    private String message;
    private String token;
    private UserDTO user;
    private CompanyDTO company;
    private List<ApplicationDTO> applications;

    public static AuthResponse success(String token, UserDTO user, CompanyDTO company,
                                       List<ApplicationDTO> applications) {
        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setToken(token);
        response.setUser(user);
        response.setCompany(company);
        response.setApplications(applications);
        return response;
    }

    public static AuthResponse error(String message) {
        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public CompanyDTO getCompany() { return company; }
    public void setCompany(CompanyDTO company) { this.company = company; }

    public List<ApplicationDTO> getApplications() { return applications; }
    public void setApplications(List<ApplicationDTO> applications) { this.applications = applications; }
}
