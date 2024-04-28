package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.entity.Role;
import com.Back.PorscheDashboardBackend.entity.User;
import com.Back.PorscheDashboardBackend.service.RoleService;
import com.Back.PorscheDashboardBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;


    @PostMapping({"/createNewRole"})
    @PreAuthorize("hasAnyRole('AdminUser')")
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);

    }
}





