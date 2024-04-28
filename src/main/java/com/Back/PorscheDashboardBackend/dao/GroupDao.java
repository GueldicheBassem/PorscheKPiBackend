package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Groupe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupDao extends CrudRepository<Groupe,Long> {
    List<Groupe> findAll();


    Groupe findByGroupName(String groupName);
}
