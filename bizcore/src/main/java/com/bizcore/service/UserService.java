package com.bizcore.service;

import com.bizcore.dto.CreateUserRequest;
import com.bizcore.dto.UpdateUserRequest;
import com.bizcore.dto.UserDTO;
import com.bizcore.entity.Application;
import com.bizcore.entity.Company;
import com.bizcore.entity.Role;
import com.bizcore.entity.User;
import com.bizcore.repository.ApplicationRepository;
import com.bizcore.repository.CompanyRepository;
import com.bizcore.repository.RoleRepository;
import com.bizcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<UserDTO> getUsersByCompany(Long companyId, Pageable pageable) {
        return userRepository.findByCompanyId(companyId, pageable).map(this::convertToDTO);
    }

    public List<UserDTO> getActiveUsersByCompany(Long companyId) {
        return userRepository.findByCompanyIdAndActiveTrue(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<UserDTO> getUserByUsername(Long companyId, String username) {
        return userRepository.findByCompanyIdAndUsername(companyId, username)
                .map(this::convertToDTO);
    }

    public UserDTO createUser(CreateUserRequest request) {
        // Validate company exists
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Check if username already exists in company
        if (userRepository.findByCompanyIdAndUsername(request.getCompanyId(), request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists in this company");
        }

        // Check if email already exists in company
        if (userRepository.findByCompanyIdAndEmail(request.getCompanyId(), request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists in this company");
        }

        User user = new User();
        user.setCompany(company);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setActive(true);
        user.setEmailVerified(false);

        // Assign roles - at least one role is required
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new RuntimeException("At least one role must be assigned to the user");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        if (roles.isEmpty()) {
            throw new RuntimeException("No valid roles found with the provided role IDs");
        }

        // Verify all roles belong to the same company
        for (Role role : roles) {
            if (!role.getCompany().getId().equals(request.getCompanyId())) {
                throw new RuntimeException("Role " + role.getName() + " does not belong to the specified company");
            }
        }

        user.setRoles(roles);

        // Assign applications
        if (request.getApplicationIds() != null && !request.getApplicationIds().isEmpty()) {
            Set<Application> applications = new HashSet<>(applicationRepository.findAllById(request.getApplicationIds()));
            user.setApplications(applications);
        }

        User savedUser = userRepository.save(user);

        // Audit log
        auditService.log(
                company.getId(),
                savedUser.getId(),
                savedUser.getUsername(),
                null,
                "USER_CREATED",
                "User",
                savedUser.getId().toString(),
                "Created new user: " + savedUser.getUsername(),
                null,
                null,
                null,
                null
        );

        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update basic fields
        if (request.getEmail() != null) {
            // Check if email is being changed and if it already exists
            if (!request.getEmail().equals(user.getEmail())) {
                if (userRepository.findByCompanyIdAndEmail(user.getCompany().getId(), request.getEmail()).isPresent()) {
                    throw new RuntimeException("Email already exists in this company");
                }
                user.setEmail(request.getEmail());
            }
        }

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getActive() != null) user.setActive(request.getActive());

        // Update roles - at least one role is required
        if (request.getRoleIds() != null) {
            if (request.getRoleIds().isEmpty()) {
                throw new RuntimeException("At least one role must be assigned to the user");
            }

            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            if (roles.isEmpty()) {
                throw new RuntimeException("No valid roles found with the provided role IDs");
            }

            // Verify all roles belong to the same company
            for (Role role : roles) {
                if (!role.getCompany().getId().equals(user.getCompany().getId())) {
                    throw new RuntimeException("Role " + role.getName() + " does not belong to the user's company");
                }
            }

            user.setRoles(roles);
        }

        // Update applications
        if (request.getApplicationIds() != null) {
            Set<Application> applications = new HashSet<>(applicationRepository.findAllById(request.getApplicationIds()));
            user.setApplications(applications);
        }

        User updatedUser = userRepository.save(user);

        // Audit log
        auditService.log(
                user.getCompany().getId(),
                updatedUser.getId(),
                updatedUser.getUsername(),
                null,
                "USER_UPDATED",
                "User",
                updatedUser.getId().toString(),
                "Updated user: " + updatedUser.getUsername(),
                null,
                null,
                null,
                null
        );

        return convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Audit log before deletion
        auditService.log(
                user.getCompany().getId(),
                user.getId(),
                user.getUsername(),
                null,
                "USER_DELETED",
                "User",
                user.getId().toString(),
                "Deleted user: " + user.getUsername(),
                null,
                null,
                null,
                null
        );

        userRepository.delete(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);

        // Audit log
        auditService.log(
                user.getCompany().getId(),
                user.getId(),
                user.getUsername(),
                null,
                "USER_DEACTIVATED",
                "User",
                user.getId().toString(),
                "Deactivated user: " + user.getUsername(),
                null,
                null,
                null,
                null
        );
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);
        userRepository.save(user);

        // Audit log
        auditService.log(
                user.getCompany().getId(),
                user.getId(),
                user.getUsername(),
                null,
                "USER_ACTIVATED",
                "User",
                user.getId().toString(),
                "Activated user: " + user.getUsername(),
                null,
                null,
                null,
                null
        );
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setCompanyCode(user.getCompany().getCode());
        dto.setCompanyName(user.getCompany().getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAvatar(user.getAvatar());
        dto.setActive(user.isActive());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // Convert roles to list of role names
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        // Convert roles to list of role IDs
        dto.setRoleIds(user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList()));

        // Convert applications to list of app names
        dto.setApplications(user.getApplications().stream()
                .map(Application::getName)
                .collect(Collectors.toList()));

        return dto;
    }
}
