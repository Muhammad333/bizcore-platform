package com.bizcore.service;

import com.bizcore.dto.TranslationDTO;
import com.bizcore.entity.Translation;
import com.bizcore.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TranslationService {

    @Autowired
    private TranslationRepository translationRepository;

    /**
     * Get translation by key and language
     * If not found, returns the key itself
     */
    public String translate(String key, String language) {
        return translationRepository.findByKeyAndLanguage(key, language)
                .map(Translation::getValue)
                .orElse(key);
    }

    /**
     * Get all translations for a specific language
     * Returns as a map of key -> value for easy frontend consumption
     */
    public Map<String, String> getTranslationsForLanguage(String language) {
        List<Translation> translations = translationRepository.findByLanguage(language);
        Map<String, String> translationMap = new HashMap<>();

        for (Translation translation : translations) {
            translationMap.put(translation.getKey(), translation.getValue());
        }

        return translationMap;
    }

    /**
     * Get all translations for a specific language and module
     */
    public Map<String, String> getTranslationsForLanguageAndModule(String language, String module) {
        List<Translation> translations = translationRepository.findByLanguageAndModule(language, module);
        Map<String, String> translationMap = new HashMap<>();

        for (Translation translation : translations) {
            translationMap.put(translation.getKey(), translation.getValue());
        }

        return translationMap;
    }

    /**
     * Get all translations (for admin management)
     */
    public List<TranslationDTO> getAllTranslations() {
        return translationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all translations for a specific language (as DTOs)
     */
    public List<TranslationDTO> getTranslationsByLanguage(String language) {
        return translationRepository.findByLanguage(language).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create or update translation
     */
    public TranslationDTO saveTranslation(TranslationDTO dto) {
        Translation translation = translationRepository
                .findByKeyAndLanguage(dto.getKey(), dto.getLanguage())
                .orElse(new Translation());

        translation.setKey(dto.getKey());
        translation.setLanguage(dto.getLanguage());
        translation.setValue(dto.getValue());
        translation.setModule(dto.getModule());

        Translation saved = translationRepository.save(translation);
        return convertToDTO(saved);
    }

    /**
     * Delete translation
     */
    public void deleteTranslation(Long id) {
        translationRepository.deleteById(id);
    }

    /**
     * Get available languages
     */
    public List<String> getAvailableLanguages() {
        return translationRepository.findAll().stream()
                .map(Translation::getLanguage)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO
     */
    private TranslationDTO convertToDTO(Translation translation) {
        return new TranslationDTO(
                translation.getId(),
                translation.getKey(),
                translation.getLanguage(),
                translation.getValue(),
                translation.getModule(),
                translation.getCreatedAt(),
                translation.getUpdatedAt()
        );
    }
}
