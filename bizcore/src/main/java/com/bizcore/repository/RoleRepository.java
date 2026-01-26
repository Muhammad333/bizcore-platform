package com.bizcore.repository;

import com.bizcore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCompanyIdAndName(Long companyId, String name);

    List<Role> findByCompanyId(Long companyId);

    List<Role> findByCompanyIdAndActiveTrue(Long companyId);

    List<Role> findBySystemRoleTrue();

    boolean existsByCompanyIdAndName(Long companyId, String name);

    Optional<Role> findByCompanyIsNullAndName(String name);

    List<Role> findByCompanyIsNull();
}
