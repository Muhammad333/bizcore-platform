package com.bizcore.service;

import com.bizcore.dto.AuditLogDTO;
import com.bizcore.entity.AuditLog;
import com.bizcore.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(Long companyId, Long userId, String username, String appCode,
                    String action, String entityType, String entityId,
                    String description, String oldValue, String newValue,
                    String ipAddress, String userAgent) {
        AuditLog log = new AuditLog();
        log.setCompanyId(companyId);
        log.setUserId(userId);
        log.setUsername(username);
        log.setApplicationCode(appCode);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        auditLogRepository.save(log);
    }

    public void logSimple(Long companyId, Long userId, String username, String action, String description) {
        log(companyId, userId, username, null, action, null, null, description, null, null, null, null);
    }

    public Page<AuditLogDTO> getAuditLogsByCompany(Long companyId, Pageable pageable) {
        return auditLogRepository.findByCompanyIdOrderByTimestampDesc(companyId, pageable)
            .map(this::convertToDTO);
    }

    public Page<AuditLogDTO> getAuditLogsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable)
            .map(this::convertToDTO);
    }

    public Page<AuditLogDTO> getAuditLogsByApplication(String applicationCode, Pageable pageable) {
        return auditLogRepository.findByApplicationCodeOrderByTimestampDesc(applicationCode, pageable)
            .map(this::convertToDTO);
    }

    private AuditLogDTO convertToDTO(AuditLog log) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(log.getId());
        dto.setCompanyId(log.getCompanyId());
        dto.setUserId(log.getUserId());
        dto.setUsername(log.getUsername());
        dto.setApplicationCode(log.getApplicationCode());
        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());
        dto.setDescription(log.getDescription());
        dto.setOldValue(log.getOldValue());
        dto.setNewValue(log.getNewValue());
        dto.setIpAddress(log.getIpAddress());
        dto.setUserAgent(log.getUserAgent());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
