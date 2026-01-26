package com.bizcore.controller;

import com.bizcore.dto.TranslationDTO;
import com.bizcore.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    /**
     * Get all translations for a specific language (as key-value map)
     * Public endpoint - no authentication required
     */
    @GetMapping("/{language}")
    public ResponseEntity<Map<String, String>> getTranslations(@PathVariable String language) {
        Map<String, String> translations = translationService.getTranslationsForLanguage(language);
        return ResponseEntity.ok(translations);
    }

    /**
     * Get translations for a specific language and module
     * Public endpoint - no authentication required
     */
    @GetMapping("/{language}/{module}")
    public ResponseEntity<Map<String, String>> getTranslationsByModule(
            @PathVariable String language,
            @PathVariable String module
    ) {
        Map<String, String> translations = translationService.getTranslationsForLanguageAndModule(language, module);
        return ResponseEntity.ok(translations);
    }

    /**
     * Get all translations (for admin management)
     * Requires ADMIN role
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGE', 'USER_VIEW')")
    public ResponseEntity<List<TranslationDTO>> getAllTranslations() {
        List<TranslationDTO> translations = translationService.getAllTranslations();
        return ResponseEntity.ok(translations);
    }

    /**
     * Get available languages
     * Public endpoint
     */
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getAvailableLanguages() {
        List<String> languages = translationService.getAvailableLanguages();
        return ResponseEntity.ok(languages);
    }

    /**
     * Create or update translation
     * Requires ADMIN role
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<TranslationDTO> saveTranslation(@RequestBody TranslationDTO dto) {
        TranslationDTO saved = translationService.saveTranslation(dto);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete translation
     * Requires ADMIN role
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.ok().build();
    }
}
