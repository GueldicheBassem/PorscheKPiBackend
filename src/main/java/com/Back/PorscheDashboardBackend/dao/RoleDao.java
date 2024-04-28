package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {

    List<Role> findAll();


}
