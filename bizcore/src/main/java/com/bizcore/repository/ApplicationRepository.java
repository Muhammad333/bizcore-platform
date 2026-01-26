package com.bizcore.repository;

import com.bizcore.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByCode(String code);

    boolean existsByCode(String code);

    List<Application> findByActiveTrue();

    List<Application> findByActiveTrueOrderByDisplayOrderAsc();

    List<Application> findByPublicAccessTrue();

    @Query("SELECT a FROM Application a JOIN a.users u WHERE u.id = :userId AND a.active = true ORDER BY a.displayOrder")
    List<Application> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Application a JOIN a.users u WHERE u.company.id = :companyId AND a.active = true")
    List<Application> findByCompanyId(@Param("companyId") Long companyId);
}
