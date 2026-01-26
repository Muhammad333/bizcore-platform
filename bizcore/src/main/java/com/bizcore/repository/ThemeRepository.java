package com.bizcore.repository;

import com.bizcore.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findByCode(String code);

    Optional<Theme> findByDefaultThemeTrue();

    List<Theme> findByActiveTrue();

    List<Theme> findByDarkMode(boolean darkMode);

    boolean existsByCode(String code);
}
