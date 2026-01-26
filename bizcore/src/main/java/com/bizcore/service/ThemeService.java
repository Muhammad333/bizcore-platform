package com.bizcore.service;

import com.bizcore.dto.CreateThemeRequest;
import com.bizcore.dto.ThemeDTO;
import com.bizcore.dto.UpdateThemeRequest;
import com.bizcore.entity.Theme;
import com.bizcore.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private AuditService auditService;

    public Page<ThemeDTO> getAllThemes(Pageable pageable) {
        return themeRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<ThemeDTO> getActiveThemes() {
        return themeRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ThemeDTO> getDefaultTheme() {
        return themeRepository.findByDefaultThemeTrue().map(this::convertToDTO);
    }

    public Optional<ThemeDTO> getThemeById(Long id) {
        return themeRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<ThemeDTO> getThemeByCode(String code) {
        return themeRepository.findByCode(code).map(this::convertToDTO);
    }

    public ThemeDTO createTheme(CreateThemeRequest request) {
        if (themeRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Theme code already exists");
        }

        Theme theme = new Theme();
        theme.setCode(request.getCode());
        theme.setName(request.getName());
        theme.setDescription(request.getDescription());

        if (request.getPrimaryColor() != null) theme.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) theme.setSecondaryColor(request.getSecondaryColor());
        if (request.getAccentColor() != null) theme.setAccentColor(request.getAccentColor());
        if (request.getBackgroundColor() != null) theme.setBackgroundColor(request.getBackgroundColor());
        if (request.getTextColor() != null) theme.setTextColor(request.getTextColor());
        if (request.getErrorColor() != null) theme.setErrorColor(request.getErrorColor());
        if (request.getSuccessColor() != null) theme.setSuccessColor(request.getSuccessColor());
        if (request.getWarningColor() != null) theme.setWarningColor(request.getWarningColor());
        if (request.getFontFamily() != null) theme.setFontFamily(request.getFontFamily());
        if (request.getBorderRadius() != null) theme.setBorderRadius(request.getBorderRadius());
        if (request.getDarkMode() != null) theme.setDarkMode(request.getDarkMode());
        if (request.getCustomCSS() != null) theme.setCustomCss(request.getCustomCSS());

        theme.setActive(true);
        theme.setDefaultTheme(false);

        if (request.getDefaultTheme() != null && request.getDefaultTheme()) {
            themeRepository.findByDefaultThemeTrue().ifPresent(existingDefault -> {
                existingDefault.setDefaultTheme(false);
                themeRepository.save(existingDefault);
            });
            theme.setDefaultTheme(true);
        }

        Theme savedTheme = themeRepository.save(theme);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "THEME_CREATED",
                "Created new theme: " + savedTheme.getName()
        );

        return convertToDTO(savedTheme);
    }

    public ThemeDTO updateTheme(Long id, UpdateThemeRequest request) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        if (request.getName() != null) theme.setName(request.getName());
        if (request.getDescription() != null) theme.setDescription(request.getDescription());
        if (request.getPrimaryColor() != null) theme.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) theme.setSecondaryColor(request.getSecondaryColor());
        if (request.getAccentColor() != null) theme.setAccentColor(request.getAccentColor());
        if (request.getBackgroundColor() != null) theme.setBackgroundColor(request.getBackgroundColor());
        if (request.getTextColor() != null) theme.setTextColor(request.getTextColor());
        if (request.getErrorColor() != null) theme.setErrorColor(request.getErrorColor());
        if (request.getSuccessColor() != null) theme.setSuccessColor(request.getSuccessColor());
        if (request.getWarningColor() != null) theme.setWarningColor(request.getWarningColor());
        if (request.getFontFamily() != null) theme.setFontFamily(request.getFontFamily());
        if (request.getBorderRadius() != null) theme.setBorderRadius(request.getBorderRadius());
        if (request.getDarkMode() != null) theme.setDarkMode(request.getDarkMode());
        if (request.getActive() != null) theme.setActive(request.getActive());
        if (request.getCustomCSS() != null) theme.setCustomCss(request.getCustomCSS());

        if (request.getDefaultTheme() != null && request.getDefaultTheme()) {
            themeRepository.findByDefaultThemeTrue().ifPresent(existingDefault -> {
                if (!existingDefault.getId().equals(theme.getId())) {
                    existingDefault.setDefaultTheme(false);
                    themeRepository.save(existingDefault);
                }
            });
            theme.setDefaultTheme(true);
        } else if (request.getDefaultTheme() != null && !request.getDefaultTheme()) {
            theme.setDefaultTheme(false);
        }

        Theme updatedTheme = themeRepository.save(theme);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "THEME_UPDATED",
                "Updated theme: " + updatedTheme.getName()
        );

        return convertToDTO(updatedTheme);
    }

    public void deleteTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        if (theme.isDefaultTheme()) {
            throw new RuntimeException("Cannot delete default theme");
        }

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "THEME_DELETED",
                "Deleted theme: " + theme.getName()
        );

        themeRepository.delete(theme);
    }

    private ThemeDTO convertToDTO(Theme theme) {
        ThemeDTO dto = new ThemeDTO();
        dto.setId(theme.getId());
        dto.setCode(theme.getCode());
        dto.setName(theme.getName());
        dto.setDescription(theme.getDescription());
        dto.setPrimaryColor(theme.getPrimaryColor());
        dto.setSecondaryColor(theme.getSecondaryColor());
        dto.setAccentColor(theme.getAccentColor());
        dto.setBackgroundColor(theme.getBackgroundColor());
        dto.setTextColor(theme.getTextColor());
        dto.setErrorColor(theme.getErrorColor());
        dto.setSuccessColor(theme.getSuccessColor());
        dto.setWarningColor(theme.getWarningColor());
        dto.setFontFamily(theme.getFontFamily());
        dto.setBorderRadius(theme.getBorderRadius());
        dto.setDarkMode(theme.isDarkMode());
        dto.setDefaultTheme(theme.isDefaultTheme());
        dto.setActive(theme.isActive());
        dto.setCustomCss(theme.getCustomCss());
        return dto;
    }
}
