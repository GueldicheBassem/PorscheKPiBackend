package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.dao.RoleDao;
import com.Back.PorscheDashboardBackend.dao.UserDao;
import com.Back.PorscheDashboardBackend.entity.Role;
import com.Back.PorscheDashboardBackend.entity.User;
import com.Back.PorscheDashboardBackend.service.GroupService;
import com.Back.PorscheDashboardBackend.service.RoleService;
import com.Back.PorscheDashboardBackend.service.UserService;


import javax.annotation.PostConstruct;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRoleAndUser();
    }
    @PostConstruct
    public void InitGroup(){
        groupService.InitGroup();
    }


    @PostMapping({"/registerNewUser"})
    @PreAuthorize("hasAnyRole('AdminUser')")
    public User registerNewUser(@RequestBody User user){
        return userService.registerNewUser(user);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public List<User> getAllUsersExceptAdmin() {
        return userService.getAllUsersExceptAdmin();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }
@Transactional
    @DeleteMapping("/deleteUser/{userName}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public void deleteUser(@PathVariable String userName) {
        userDao.deleteByUserName(userName);
    }

    @PutMapping("/updateUser/{userName}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public User updateUser(@PathVariable String userName, @RequestBody User updatedUser) {
        return userService.updateUser(userName, updatedUser);
    }

    @GetMapping("/getUser/{userName}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public User getUserByUsername(@PathVariable String userName) {
        return userDao.findByUserName(userName);
    }


}
