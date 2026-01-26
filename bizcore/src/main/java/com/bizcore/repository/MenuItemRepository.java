package com.bizcore.repository;

import com.bizcore.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByApplicationIdAndParentIsNullAndActiveTrueOrderByDisplayOrderAsc(Long applicationId);

    List<MenuItem> findByApplicationIdAndActiveTrueOrderByDisplayOrderAsc(Long applicationId);

    List<MenuItem> findByParentIdAndActiveTrueOrderByDisplayOrderAsc(Long parentId);

    Optional<MenuItem> findByApplicationIdAndCode(Long applicationId, String code);

    @Query("SELECT m FROM MenuItem m JOIN m.allowedRoles r WHERE m.application.id = :appId AND r.id IN :roleIds AND m.active = true AND m.parent IS NULL ORDER BY m.displayOrder")
    List<MenuItem> findByApplicationAndRoles(@Param("appId") Long applicationId, @Param("roleIds") List<Long> roleIds);

    @Query("SELECT m FROM MenuItem m WHERE m.application.code = :appCode AND m.active = true AND m.parent IS NULL ORDER BY m.displayOrder")
    List<MenuItem> findRootMenusByApplicationCode(@Param("appCode") String applicationCode);

    boolean existsByApplicationIdAndCode(Long applicationId, String code);
}
