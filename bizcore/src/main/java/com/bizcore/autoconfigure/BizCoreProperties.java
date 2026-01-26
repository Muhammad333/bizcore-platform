package com.bizcore.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for BizCore.
 */
@ConfigurationProperties(prefix = "bizcore")
public class BizCoreProperties {

    /**
     * Application code for this project (e.g., CHATBOT, SUPPLYMATE)
     */
    private String appCode = "BIZCORE";

    /**
     * Application name
     */
    private String appName = "BizCore Admin";

    /**
     * Enable/disable BizCore security
     */
    private boolean securityEnabled = true;

    /**
     * Enable/disable demo data loading
     */
    private boolean loadDemoData = true;

    /**
     * JWT secret key
     */
    private String jwtSecret = "bizcore-jwt-secret-key-2024-very-long-and-secure";

    /**
     * JWT expiration in milliseconds (default 24 hours)
     */
    private long jwtExpiration = 86400000;

    // Getters and Setters
    public String getAppCode() { return appCode; }
    public void setAppCode(String appCode) { this.appCode = appCode; }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public boolean isSecurityEnabled() { return securityEnabled; }
    public void setSecurityEnabled(boolean securityEnabled) { this.securityEnabled = securityEnabled; }

    public boolean isLoadDemoData() { return loadDemoData; }
    public void setLoadDemoData(boolean loadDemoData) { this.loadDemoData = loadDemoData; }

    public String getJwtSecret() { return jwtSecret; }
    public void setJwtSecret(String jwtSecret) { this.jwtSecret = jwtSecret; }

    public long getJwtExpiration() { return jwtExpiration; }
    public void setJwtExpiration(long jwtExpiration) { this.jwtExpiration = jwtExpiration; }
}
