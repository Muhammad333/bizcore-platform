package com.bizcore.service;

import com.bizcore.dto.CreateRoleRequest;
import com.bizcore.dto.RoleDTO;
import com.bizcore.dto.UpdateRoleRequest;
import com.bizcore.entity.Company;
import com.bizcore.entity.Permission;
import com.bizcore.entity.Role;
import com.bizcore.repository.CompanyRepository;
import com.bizcore.repository.PermissionRepository;
import com.bizcore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AuditService auditService;

    public Page<RoleDTO> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<RoleDTO> getRolesByCompany(Long companyId) {
        return roleRepository.findByCompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getActiveRolesByCompany(Long companyId) {
        return roleRepository.findByCompanyIdAndActiveTrue(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getSystemRoles() {
        return roleRepository.findBySystemRoleTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<RoleDTO> getRoleById(Long id) {
        return roleRepository.findById(id).map(this::convertToDTO);
    }

    public RoleDTO createRole(CreateRoleRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (roleRepository.existsByCompanyIdAndName(request.getCompanyId(), request.getName())) {
            throw new RuntimeException("Role name already exists in this company");
        }

        Role role = new Role();
        role.setCompany(company);
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setActive(true);
        role.setSystemRole(false);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);

        auditService.logSimple(
                company.getId(),
                null,
                "SYSTEM",
                "ROLE_CREATED",
                "Created new role: " + savedRole.getName()
        );

        return convertToDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (role.isSystemRole()) {
            throw new RuntimeException("Cannot modify system roles");
        }

        if (request.getName() != null) {
            if (!request.getName().equals(role.getName())) {
                if (roleRepository.existsByCompanyIdAndName(role.getCompany().getId(), request.getName())) {
                    throw new RuntimeException("Role name already exists in this company");
                }
                role.setName(request.getName());
            }
        }

        if (request.getDescription() != null) role.setDescription(request.getDescription());
        if (request.getActive() != null) role.setActive(request.getActive());

        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);

        auditService.logSimple(
                role.getCompany().getId(),
                null,
                "SYSTEM",
                "ROLE_UPDATED",
                "Updated role: " + updatedRole.getName()
        );

        return convertToDTO(updatedRole);
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (role.isSystemRole()) {
            throw new RuntimeException("Cannot delete system roles");
        }

        if (!role.getUsers().isEmpty()) {
            throw new RuntimeException("Cannot delete role with assigned users");
        }

        auditService.logSimple(
                role.getCompany().getId(),
                null,
                "SYSTEM",
                "ROLE_DELETED",
                "Deleted role: " + role.getName()
        );

        roleRepository.delete(role);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());

        if (role.getCompany() != null) {
            dto.setCompanyId(role.getCompany().getId());
            dto.setCompanyName(role.getCompany().getName());
        }

        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setSystemRole(role.isSystemRole());
        dto.setActive(role.isActive());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());

        dto.setPermissions(role.getPermissions().stream()
                .map(Permission::getCode)
                .collect(Collectors.toList()));

        dto.setUserCount(role.getUsers().size());

        return dto;
    }
}
