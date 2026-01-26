package com.bizcore.repository;

import com.bizcore.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * Find translation by key and language
     */
    Optional<Translation> findByKeyAndLanguage(String key, String language);

    /**
     * Find all translations for a specific language
     */
    List<Translation> findByLanguage(String language);

    /**
     * Find all translations for a specific module
     */
    List<Translation> findByModule(String module);

    /**
     * Find all translations for a specific language and module
     */
    List<Translation> findByLanguageAndModule(String language, String module);

    /**
     * Check if translation exists for key and language
     */
    boolean existsByKeyAndLanguage(String key, String language);
}
