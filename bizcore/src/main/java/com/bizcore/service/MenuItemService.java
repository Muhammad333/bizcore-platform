package com.bizcore.service;

import com.bizcore.dto.CreateMenuItemRequest;
import com.bizcore.dto.MenuItemDTO;
import com.bizcore.dto.UpdateMenuItemRequest;
import com.bizcore.entity.Application;
import com.bizcore.entity.MenuItem;
import com.bizcore.entity.Role;
import com.bizcore.repository.ApplicationRepository;
import com.bizcore.repository.MenuItemRepository;
import com.bizcore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditService auditService;

    public Page<MenuItemDTO> getAllMenuItems(Pageable pageable) {
        return menuItemRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<MenuItemDTO> getMenuItemsByApplication(Long applicationId) {
        return menuItemRepository.findByApplicationIdAndActiveTrueOrderByDisplayOrderAsc(applicationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getRootMenuItems(Long applicationId) {
        return menuItemRepository.findByApplicationIdAndParentIsNullAndActiveTrueOrderByDisplayOrderAsc(applicationId).stream()
                .map(this::convertToDTOWithChildren)
                .collect(Collectors.toList());
    }

    public Optional<MenuItemDTO> getMenuItemById(Long id) {
        return menuItemRepository.findById(id).map(this::convertToDTO);
    }

    public MenuItemDTO createMenuItem(CreateMenuItemRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (menuItemRepository.existsByApplicationIdAndCode(request.getApplicationId(), request.getCode())) {
            throw new RuntimeException("Menu code already exists in this application");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setApplication(application);
        menuItem.setCode(request.getCode());
        menuItem.setTitle(request.getTitle());
        menuItem.setIcon(request.getIcon());
        menuItem.setPath(request.getPath());
        menuItem.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        menuItem.setRequiredPermission(request.getRequiredPermission());
        menuItem.setActive(true);
        menuItem.setVisible(true);

        if (request.getParentId() != null) {
            MenuItem parent = menuItemRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu item not found"));
            menuItem.setParent(parent);
        }

        if (request.getAllowedRoleIds() != null && !request.getAllowedRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getAllowedRoleIds()));
            menuItem.setAllowedRoles(roles);
        }

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "MENU_CREATED",
                "Created new menu item: " + savedMenuItem.getCode() + " in application: " + application.getName()
        );

        return convertToDTO(savedMenuItem);
    }

    public MenuItemDTO updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (request.getTitle() != null) menuItem.setTitle(request.getTitle());
        if (request.getIcon() != null) menuItem.setIcon(request.getIcon());
        if (request.getPath() != null) menuItem.setPath(request.getPath());
        if (request.getDisplayOrder() != null) menuItem.setDisplayOrder(request.getDisplayOrder());
        if (request.getActive() != null) menuItem.setActive(request.getActive());
        if (request.getVisible() != null) menuItem.setVisible(request.getVisible());
        if (request.getRequiredPermission() != null) menuItem.setRequiredPermission(request.getRequiredPermission());

        if (request.getAllowedRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getAllowedRoleIds()));
            menuItem.setAllowedRoles(roles);
        }

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "MENU_UPDATED",
                "Updated menu item: " + updatedMenuItem.getCode()
        );

        return convertToDTO(updatedMenuItem);
    }

    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (!menuItem.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete menu item with children");
        }

        auditService.logSimple(
                null,
                null,
                "SYSTEM",
                "MENU_DELETED",
                "Deleted menu item: " + menuItem.getCode()
        );

        menuItemRepository.delete(menuItem);
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setApplicationId(menuItem.getApplication().getId());
        dto.setApplicationCode(menuItem.getApplication().getCode());
        dto.setCode(menuItem.getCode());
        dto.setTitle(menuItem.getTitle());
        dto.setIcon(menuItem.getIcon());
        dto.setPath(menuItem.getPath());
        dto.setDisplayOrder(menuItem.getDisplayOrder());
        dto.setActive(menuItem.isActive());
        dto.setVisible(menuItem.isVisible());
        dto.setRequiredPermission(menuItem.getRequiredPermission());

        if (menuItem.getParent() != null) {
            dto.setParentId(menuItem.getParent().getId());
        }

        dto.setAllowedRoles(menuItem.getAllowedRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return dto;
    }

    private MenuItemDTO convertToDTOWithChildren(MenuItem menuItem) {
        MenuItemDTO dto = convertToDTO(menuItem);

        if (menuItem.getChildren() != null && !menuItem.getChildren().isEmpty()) {
            List<MenuItemDTO> children = menuItem.getChildren().stream()
                    .filter(MenuItem::isActive)
                    .map(this::convertToDTOWithChildren)
                    .collect(Collectors.toList());
            dto.setChildren(children);
        } else {
            dto.setChildren(new ArrayList<>());
        }

        return dto;
    }
}
