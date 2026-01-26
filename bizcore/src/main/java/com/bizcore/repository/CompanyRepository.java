package com.bizcore.repository;

import com.bizcore.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCode(String code);
    Optional<Company> findByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
    List<Company> findByActiveTrue();
}
