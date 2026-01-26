package com.bizcore.filter;

import com.bizcore.entity.RequestLog;
import com.bizcore.repository.RequestLogRepository;
import com.bizcore.security.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Skip logging for static resources and health checks
        String requestUri = httpRequest.getRequestURI();
        if (shouldSkipLogging(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        // Wrap request and response to cache content
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Log request asynchronously
            logRequest(wrappedRequest, wrappedResponse, duration);

            // Copy response body back to the response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request,
                           ContentCachingResponseWrapper response,
                           long duration) {
        try {
            RequestLog log = new RequestLog();

            // Extract user info from JWT token
            String token = extractToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsername(token);
                Long userId = jwtTokenProvider.getUserId(token);
                Long companyId = jwtTokenProvider.getCompanyId(token);

                log.setUsername(username);
                log.setUserId(userId);
                log.setCompanyId(companyId);
            }

            // Request details
            log.setMethod(request.getMethod());
            log.setEndpoint(request.getRequestURI());
            log.setQueryParams(request.getQueryString());

            // Request body (limit size and sanitize sensitive data)
            String requestBody = getRequestBody(request);
            if (requestBody != null) {
                requestBody = sanitizeSensitiveData(requestBody);
                if (requestBody.length() > 5000) {
                    requestBody = requestBody.substring(0, 5000) + "... [truncated]";
                }
            }
            log.setRequestBody(requestBody);

            // Response details
            log.setResponseStatus(response.getStatus());

            // Response body (limit size and sanitize sensitive data)
            String responseBody = getResponseBody(response);
            if (responseBody != null) {
                responseBody = sanitizeSensitiveData(responseBody);
                if (responseBody.length() > 5000) {
                    responseBody = responseBody.substring(0, 5000) + "... [truncated]";
                }
            }
            log.setResponseBody(responseBody);

            // Client info
            log.setIpAddress(getClientIpAddress(request));
            log.setUserAgent(request.getHeader("User-Agent"));

            // Duration
            log.setDurationMs(duration);

            // Error message (if error response)
            if (response.getStatus() >= 400) {
                log.setErrorMessage("HTTP " + response.getStatus() + " Error");
            }

            // Save to database asynchronously
            requestLogRepository.save(log);

        } catch (Exception e) {
            // Don't let logging errors break the application
            logger.error("Failed to log request", e);
        }
    }

    private boolean shouldSkipLogging(String uri) {
        return uri.startsWith("/app/assets/") ||
               uri.startsWith("/css/") ||
               uri.startsWith("/js/") ||
               uri.startsWith("/images/") ||
               uri.startsWith("/favicon.ico") ||
               uri.startsWith("/actuator/health") ||
               uri.endsWith(".css") ||
               uri.endsWith(".js") ||
               uri.endsWith(".png") ||
               uri.endsWith(".jpg") ||
               uri.endsWith(".jpeg") ||
               uri.endsWith(".gif") ||
               uri.endsWith(".svg") ||
               uri.endsWith(".woff") ||
               uri.endsWith(".woff2") ||
               uri.endsWith(".ttf") ||
               uri.endsWith(".eot");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            byte[] buf = request.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.debug("Failed to read request body", e);
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        try {
            byte[] buf = response.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.debug("Failed to read response body", e);
        }
        return null;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * Sanitize sensitive data from request/response bodies
     * Masks passwords, tokens, credit cards, etc.
     */
    private String sanitizeSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }

        try {
            // List of sensitive field names to mask
            String[] sensitiveFields = {
                "password", "passwordConfirm", "currentPassword", "newPassword", "oldPassword",
                "token", "accessToken", "refreshToken", "apiKey", "secretKey",
                "creditCard", "cvv", "ssn", "socialSecurityNumber",
                "secret", "privateKey", "authorization"
            };

            String sanitized = data;

            // Mask sensitive fields in JSON format
            for (String field : sensitiveFields) {
                // Match patterns like: "password":"value" or "password": "value"
                String pattern1 = "\"" + field + "\"\\s*:\\s*\"[^\"]*\"";
                sanitized = sanitized.replaceAll(pattern1, "\"" + field + "\":\"***REDACTED***\"");

                // Match patterns like: "password":value (without quotes)
                String pattern2 = "\"" + field + "\"\\s*:\\s*[^,}\\]]+";
                sanitized = sanitized.replaceAll(pattern2, "\"" + field + "\":\"***REDACTED***\"");
            }

            return sanitized;
        } catch (Exception e) {
            logger.debug("Failed to sanitize sensitive data", e);
            return data;
        }
    }
}
