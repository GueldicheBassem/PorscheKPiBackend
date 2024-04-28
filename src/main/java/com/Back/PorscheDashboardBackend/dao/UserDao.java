package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, String> {
    List<User> findAllByRoleRoleNameNot(String roleName);


    void deleteByUserName(String userName);

    User findByUserName(String userName);
}
