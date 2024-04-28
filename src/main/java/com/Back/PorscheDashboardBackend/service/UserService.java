package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.RoleDao;
import com.Back.PorscheDashboardBackend.dao.UserDao;
import com.Back.PorscheDashboardBackend.entity.Role;
import com.Back.PorscheDashboardBackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initRoleAndUser() {


        Role adminRole = new Role();
        adminRole.setRoleName("AdminUser");
        adminRole.setRoleDescription("Admin role with access to every endpoint available in the dashboard");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("NormalUser");
        userRole.setRoleDescription("Default role for newly created records with only Viewing endpoints");
        roleDao.save(userRole);


        Role KosuUser = new Role();
        KosuUser.setRoleName("KosuUser");
        KosuUser.setRoleDescription("Kosu role with access to viewing and kosu related endpoints");
        roleDao.save(KosuUser);

        User adminUser = new User();
        adminUser.setUserName("DashAdmin");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setName("AdminName");
        adminUser.setLastname("AdminLastName");
        adminUser.setMatricule(000000000000L);
        adminUser.setSuperiorEmail("eternuity@outlook.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

       User user = new User();
          user.setUserPassword(getEncodedPassword("user@pass"));
       user.setUserName("DashUser");
       user.setName("Bassem");
       user.setLastname("Gueldiche");
       user.setMatricule(22228L);
       Set<Role> userRoles = new HashSet<>();
       userRoles.add(KosuUser);
       user.setRole(userRoles);
       userDao.save(user);
    }

    public User registerNewUser(User user) {
        // Retrieve the NormalUser role

        Set<Role> userRoles = new HashSet<>();
        userRoles=user.getRole();

        user.setRole(userRoles);

        // Set additional user details
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        user.setUserName(user.getUserName());
        // Assuming the following properties are available in the User class
        // Replace these with the actual property names if they differ
        user.setName(user.getName());
        user.setLastname(user.getLastname());
        user.setSuperiorEmail(user.getSuperiorEmail());
        user.setMatricule(user.getMatricule());

        return userDao.save(user);
    }


    public List<User> getAllUsersExceptAdmin() {
        // Implement logic to retrieve all users except those with the role "AdminUser"
        return userDao.findAllByRoleRoleNameNot("AdminUser");
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User updateUser(String userName, User updatedUser) {
        // Find the existing user by username
        User existingUser = userDao.findByUserName(userName);
        if (existingUser != null) {
            // Update user details
            existingUser.setName(updatedUser.getName());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setMatricule(updatedUser.getMatricule());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setUserPassword(getEncodedPassword(updatedUser.getUserPassword()));
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setPenaltypoints(updatedUser.getPenaltypoints());
            existingUser.setSuperiorEmail(updatedUser.getSuperiorEmail());
            // Save the updated user
            return userDao.save(existingUser);
        }
        // Handle case where user with the given username is not found
        return null;
    }

}
