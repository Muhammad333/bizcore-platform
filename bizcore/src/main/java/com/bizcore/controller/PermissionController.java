package com.bizcore.controller;

import com.bizcore.dto.CreatePermissionRequest;
import com.bizcore.dto.PermissionDTO;
import com.bizcore.dto.UpdatePermissionRequest;
import com.bizcore.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGE', 'ROLE_VIEW')")
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "moduleName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(permissionService.getAllPermissions(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGE', 'ROLE_VIEW')")
    public ResponseEntity<List<PermissionDTO>> getAllPermissionsList() {
        return ResponseEntity.ok(permissionService.getAllPermissionsList());
    }

    @GetMapping("/module/{moduleName}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByModule(@PathVariable String moduleName) {
        return ResponseEntity.ok(permissionService.getPermissionsByModule(moduleName));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return permissionService.getPermissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<PermissionDTO> getPermissionByCode(@PathVariable String code) {
        return permissionService.getPermissionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        try {
            PermissionDTO permission = permissionService.createPermission(request);
            return ResponseEntity.ok(permission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePermissionRequest request
    ) {
        try {
            PermissionDTO permission = permissionService.updatePermission(id, request);
            return ResponseEntity.ok(permission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
