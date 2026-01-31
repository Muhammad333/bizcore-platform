package com.bizcore.service;

import com.bizcore.dto.*;
import com.bizcore.entity.*;
import com.bizcore.repository.*;
import com.bizcore.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

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
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuditService auditService;

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByCompanyCodeAndUsername(
            request.getCompanyCode(), request.getUsername());

        if (userOpt.isEmpty()) {
            return AuthResponse.error("Invalid company code, username, or password");
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            return AuthResponse.error("User account is disabled");
        }

        if (!user.getCompany().isActive()) {
            return AuthResponse.error("Company account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.error("Invalid company code, username, or password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        List<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        List<String> permissions = user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getCode)
            .distinct()
            .collect(Collectors.toList());

        String token = tokenProvider.generateToken(
            user.getId(),
            user.getCompany().getId(),
            user.getCompany().getCode(),
            user.getUsername(),
            roles,
            permissions
        );

        UserDTO userDTO = convertUserToDTO(user);
        CompanyDTO companyDTO = convertCompanyToDTO(user.getCompany());
        List<ApplicationDTO> apps = getApplicationsForUser(user);

        auditService.logSimple(
            user.getCompany().getId(),
            user.getId(),
            user.getUsername(),
            "LOGIN",
            "User logged in successfully"
        );

        return AuthResponse.success(token, userDTO, companyDTO, apps);
    }

    public AuthResponse registerCompany(RegisterCompanyRequest request) {
        if (companyRepository.existsByCode(request.getCompanyCode())) {
            return AuthResponse.error("Company code already exists");
        }

        Company company = new Company();
        company.setCode(request.getCompanyCode().toUpperCase());
        company.setName(request.getCompanyName());
        company.setEmail(request.getCompanyEmail());
        company.setPhone(request.getCompanyPhone());
        company.setActive(true);
        company.setSubscriptionPlan("FREE");
        Company savedCompany = companyRepository.save(company);

        Role adminRole = createDefaultRoles(savedCompany);

        User admin = new User();
        admin.setCompany(savedCompany);
        admin.setUsername(request.getAdminUsername());
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setActive(true);
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

        List<Application> allApps = applicationRepository.findByActiveTrue();
        admin.setApplications(new HashSet<>(allApps));

        User savedAdmin = userRepository.save(admin);

        List<String> roles = Collections.singletonList(adminRole.getName());
        List<String> permissions = adminRole.getPermissions().stream()
            .map(Permission::getCode)
            .collect(Collectors.toList());

        String token = tokenProvider.generateToken(
            savedAdmin.getId(),
            savedCompany.getId(),
            savedCompany.getCode(),
            savedAdmin.getUsername(),
            roles,
            permissions
        );

        UserDTO userDTO = convertUserToDTO(savedAdmin);
        CompanyDTO companyDTO = convertCompanyToDTO(savedCompany);
        List<ApplicationDTO> apps = getApplicationsForUser(savedAdmin);

        auditService.logSimple(
            savedCompany.getId(),
            savedAdmin.getId(),
            savedAdmin.getUsername(),
            "COMPANY_REGISTERED",
            "New company registered: " + savedCompany.getName()
        );

        return AuthResponse.success(token, userDTO, companyDTO, apps);
    }

    private Role createDefaultRoles(Company company) {
        List<Role> systemRoles = roleRepository.findBySystemRoleTrue();

        Role adminRole = null;
        for (Role systemRole : systemRoles) {
            Role companyRole = new Role();
            companyRole.setCompany(company);
            companyRole.setName(systemRole.getName());
            companyRole.setDescription(systemRole.getDescription());
            companyRole.setPermissions(new HashSet<>(systemRole.getPermissions()));
            companyRole.setActive(true);
            Role savedRole = roleRepository.save(companyRole);
            if ("ADMIN".equals(systemRole.getName())) {
                adminRole = savedRole;
            }
        }

        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setCompany(company);
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator with full access");
            adminRole.setActive(true);
            adminRole = roleRepository.save(adminRole);
        }

        return adminRole;
    }

    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    private UserDTO convertUserToDTO(User user) {
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
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        dto.setRoleIds(user.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        dto.setPermissions(user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getCode)
            .distinct()
            .collect(Collectors.toList()));
        dto.setApplications(user.getApplications().stream().map(Application::getCode).collect(Collectors.toList()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private CompanyDTO convertCompanyToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setCode(company.getCode());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setLogo(company.getLogo());
        dto.setEmail(company.getEmail());
        dto.setPhone(company.getPhone());
        dto.setActive(company.isActive());
        dto.setSubscriptionPlan(company.getSubscriptionPlan());
        return dto;
    }

    private List<ApplicationDTO> getApplicationsForUser(User user) {
        return user.getApplications().stream()
            .filter(Application::isActive)
            .sorted(Comparator.comparing(Application::getDisplayOrder))
            .map(this::convertApplicationToDTO)
            .collect(Collectors.toList());
    }

    private ApplicationDTO convertApplicationToDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setCode(app.getCode());
        dto.setName(app.getName());
        dto.setDescription(app.getDescription());
        dto.setIcon(app.getIcon());
        dto.setColor(app.getColor());
        dto.setBaseUrl(app.getBaseUrl());
        dto.setActive(app.isActive());
        dto.setDisplayOrder(app.getDisplayOrder());
        return dto;
    }
}
