package com.bizcore.repository;

import com.bizcore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCompanyIdAndUsername(Long companyId, String username);

    Optional<User> findByCompanyIdAndEmail(Long companyId, String email);

    @Query("SELECT u FROM User u WHERE u.company.code = :companyCode AND u.username = :username")
    Optional<User> findByCompanyCodeAndUsername(@Param("companyCode") String companyCode, @Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.company.code = :companyCode AND u.email = :email")
    Optional<User> findByCompanyCodeAndEmail(@Param("companyCode") String companyCode, @Param("email") String email);

    List<User> findByCompanyId(Long companyId);

    Page<User> findByCompanyId(Long companyId, Pageable pageable);

    List<User> findByCompanyIdAndActiveTrue(Long companyId);

    boolean existsByCompanyIdAndUsername(Long companyId, String username);

    boolean existsByCompanyIdAndEmail(Long companyId, String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.company.id = :companyId AND r.name = :roleName")
    List<User> findByCompanyIdAndRoleName(@Param("companyId") Long companyId, @Param("roleName") String roleName);
}
