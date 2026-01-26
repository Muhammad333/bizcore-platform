package com.bizcore.controller;

import com.bizcore.dto.RequestLogDTO;
import com.bizcore.entity.RequestLog;
import com.bizcore.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/request-logs")
@PreAuthorize("hasAuthority('AUDIT_VIEW')")
public class RequestLogController {

    @Autowired
    private RequestLogRepository requestLogRepository;

    @GetMapping
    public ResponseEntity<Page<RequestLogDTO>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<RequestLog> logs = requestLogRepository.findAll(pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<RequestLogDTO>> getLogsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<RequestLog> logs = requestLogRepository.findByCompanyId(companyId, pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<RequestLogDTO>> getLogsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<RequestLog> logs = requestLogRepository.findByUserId(userId, pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/errors")
    public ResponseEntity<Page<RequestLogDTO>> getErrors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RequestLog> logs = requestLogRepository.findErrors(pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/errors/company/{companyId}")
    public ResponseEntity<Page<RequestLogDTO>> getErrorsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RequestLog> logs = requestLogRepository.findErrorsByCompany(companyId, pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<RequestLogDTO>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RequestLog> logs = requestLogRepository.findByDateRange(startDate, endDate, pageable);
        Page<RequestLogDTO> dtos = logs.map(RequestLogDTO::fromEntity);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        LocalDateTime lastHour = LocalDateTime.now().minusHours(1);

        stats.put("total", requestLogRepository.count());
        stats.put("errorsLast24Hours", requestLogRepository.countErrorsSince(last24Hours));
        stats.put("errorsLastHour", requestLogRepository.countErrorsSince(lastHour));

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestLogDTO> getLog(@PathVariable Long id) {
        return requestLogRepository.findById(id)
                .map(RequestLogDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        if (requestLogRepository.existsById(id)) {
            requestLogRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupOldLogs(
            @RequestParam(defaultValue = "30") int daysOld) {

        LocalDateTime before = LocalDateTime.now().minusDays(daysOld);
        long countBefore = requestLogRepository.count();

        requestLogRepository.deleteByCreatedAtBefore(before);

        long countAfter = requestLogRepository.count();
        long deleted = countBefore - countAfter;

        Map<String, Object> result = new HashMap<>();
        result.put("deleted", deleted);
        result.put("remaining", countAfter);

        return ResponseEntity.ok(result);
    }
}
