package com.bizcore.controller;

import com.bizcore.dto.CreateThemeRequest;
import com.bizcore.dto.ThemeDTO;
import com.bizcore.dto.UpdateThemeRequest;
import com.bizcore.service.ThemeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @GetMapping
    public ResponseEntity<Page<ThemeDTO>> getAllThemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(themeService.getAllThemes(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ThemeDTO>> getActiveThemes() {
        return ResponseEntity.ok(themeService.getActiveThemes());
    }

    @GetMapping("/default")
    public ResponseEntity<ThemeDTO> getDefaultTheme() {
        return themeService.getDefaultTheme()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDTO> getThemeById(@PathVariable Long id) {
        return themeService.getThemeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ThemeDTO> getThemeByCode(@PathVariable String code) {
        return themeService.getThemeByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMPANY_EDIT')")
    public ResponseEntity<ThemeDTO> createTheme(@Valid @RequestBody CreateThemeRequest request) {
        try {
            ThemeDTO theme = themeService.createTheme(request);
            return ResponseEntity.ok(theme);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_EDIT')")
    public ResponseEntity<ThemeDTO> updateTheme(
            @PathVariable Long id,
            @Valid @RequestBody UpdateThemeRequest request
    ) {
        try {
            ThemeDTO theme = themeService.updateTheme(id, request);
            return ResponseEntity.ok(theme);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_EDIT')")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        try {
            themeService.deleteTheme(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
