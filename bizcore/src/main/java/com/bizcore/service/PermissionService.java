package com.bizcore.service;

import com.bizcore.dto.CreatePermissionRequest;
import com.bizcore.dto.PermissionDTO;
import com.bizcore.dto.UpdatePermissionRequest;
import com.bizcore.entity.Permission;
import com.bizcore.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AuditService auditService;

    public Page<PermissionDTO> getAllPermissions(Pageable pageable) {
        return permissionRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<PermissionDTO> getAllPermissionsList() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByModule(String moduleName) {
        return permissionRepository.findByModuleName(moduleName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PermissionDTO> getPermissionById(Long id) {
        return permissionRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<PermissionDTO> getPermissionByCode(String code) {
        return permissionRepository.findByCode(code).map(this::convertToDTO);
    }

    public PermissionDTO createPermission(CreatePermissionRequest request) {
        if (permissionRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Permission code already exists");
        }

        Permission permission = new Permission();
        permission.setCode(request.getCode());
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setModuleName(request.getModuleName());

        Permission savedPermission = permissionRepository.save(permission);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "PERMISSION_CREATED",
                "Created new permission: " + savedPermission.getCode()
        );

        return convertToDTO(savedPermission);
    }

    public PermissionDTO updatePermission(Long id, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        if (request.getName() != null) permission.setName(request.getName());
        if (request.getDescription() != null) permission.setDescription(request.getDescription());
        if (request.getModuleName() != null) permission.setModuleName(request.getModuleName());

        Permission updatedPermission = permissionRepository.save(permission);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "PERMISSION_UPDATED",
                "Updated permission: " + updatedPermission.getCode()
        );

        return convertToDTO(updatedPermission);
    }

    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        if (!permission.getRoles().isEmpty()) {
            throw new RuntimeException("Cannot delete permission assigned to roles");
        }

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "PERMISSION_DELETED",
                "Deleted permission: " + permission.getCode()
        );

        permissionRepository.delete(permission);
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        dto.setModuleName(permission.getModuleName());
        dto.setCreatedAt(permission.getCreatedAt());
        return dto;
    }
}
