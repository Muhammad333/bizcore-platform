package com.bizcore.controller;

import com.bizcore.dto.CreateMenuItemRequest;
import com.bizcore.dto.MenuItemDTO;
import com.bizcore.dto.UpdateMenuItemRequest;
import com.bizcore.service.MenuItemService;
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
@RequestMapping("/api/menus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    @PreAuthorize("hasAuthority('APP_VIEW')")
    public ResponseEntity<Page<MenuItemDTO>> getAllMenuItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(menuItemService.getAllMenuItems(pageable));
    }

    @GetMapping("/application/{appId}")
    @PreAuthorize("hasAuthority('APP_VIEW')")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByApplication(@PathVariable Long appId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByApplication(appId));
    }

    @GetMapping("/application/{appId}/root")
    @PreAuthorize("hasAuthority('APP_VIEW')")
    public ResponseEntity<List<MenuItemDTO>> getRootMenuItemsByApplication(@PathVariable Long appId) {
        return ResponseEntity.ok(menuItemService.getRootMenuItems(appId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APP_VIEW')")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        return menuItemService.getMenuItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('APP_MANAGE')")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
        try {
            MenuItemDTO menuItem = menuItemService.createMenuItem(request);
            return ResponseEntity.ok(menuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APP_MANAGE')")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemRequest request
    ) {
        try {
            MenuItemDTO menuItem = menuItemService.updateMenuItem(id, request);
            return ResponseEntity.ok(menuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APP_MANAGE')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
