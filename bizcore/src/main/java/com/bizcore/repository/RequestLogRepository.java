package com.bizcore.repository;

import com.bizcore.entity.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    // Find by company
    Page<RequestLog> findByCompanyId(Long companyId, Pageable pageable);

    // Find by user
    Page<RequestLog> findByUserId(Long userId, Pageable pageable);

    // Find by status code
    Page<RequestLog> findByResponseStatus(Integer status, Pageable pageable);

    // Find errors (status >= 400)
    @Query("SELECT r FROM RequestLog r WHERE r.responseStatus >= 400 ORDER BY r.createdAt DESC")
    Page<RequestLog> findErrors(Pageable pageable);

    // Find errors by company
    @Query("SELECT r FROM RequestLog r WHERE r.companyId = :companyId AND r.responseStatus >= 400 ORDER BY r.createdAt DESC")
    Page<RequestLog> findErrorsByCompany(@Param("companyId") Long companyId, Pageable pageable);

    // Find by date range
    @Query("SELECT r FROM RequestLog r WHERE r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    Page<RequestLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     Pageable pageable);

    // Find by company and date range
    @Query("SELECT r FROM RequestLog r WHERE r.companyId = :companyId AND r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    Page<RequestLog> findByCompanyAndDateRange(@Param("companyId") Long companyId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);

    // Count errors in last N hours
    @Query("SELECT COUNT(r) FROM RequestLog r WHERE r.responseStatus >= 400 AND r.createdAt >= :since")
    Long countErrorsSince(@Param("since") LocalDateTime since);

    // Count requests by user
    Long countByUserId(Long userId);

    // Delete old logs (for cleanup)
    void deleteByCreatedAtBefore(LocalDateTime before);
}
