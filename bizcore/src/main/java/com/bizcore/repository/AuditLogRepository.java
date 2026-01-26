package com.bizcore.repository;

import com.bizcore.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByCompanyIdOrderByTimestampDesc(Long companyId, Pageable pageable);

    Page<AuditLog> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    Page<AuditLog> findByApplicationCodeOrderByTimestampDesc(String applicationCode, Pageable pageable);

    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, String entityId);

    @Query("SELECT a FROM AuditLog a WHERE a.companyId = :companyId AND a.timestamp BETWEEN :start AND :end ORDER BY a.timestamp DESC")
    List<AuditLog> findByCompanyIdAndTimestampBetween(
        @Param("companyId") Long companyId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT a FROM AuditLog a WHERE a.companyId = :companyId AND a.action = :action ORDER BY a.timestamp DESC")
    Page<AuditLog> findByCompanyIdAndAction(
        @Param("companyId") Long companyId,
        @Param("action") String action,
        Pageable pageable
    );

    @Query("SELECT DISTINCT a.action FROM AuditLog a WHERE a.companyId = :companyId")
    List<String> findDistinctActionsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.companyId = :companyId AND a.action = :action AND a.timestamp > :since")
    Long countByCompanyIdAndActionSince(
        @Param("companyId") Long companyId,
        @Param("action") String action,
        @Param("since") LocalDateTime since
    );
}
