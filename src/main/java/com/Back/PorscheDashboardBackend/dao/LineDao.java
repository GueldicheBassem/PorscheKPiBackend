package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Line;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LineDao extends CrudRepository<Line, Long> {
    List<Line> findAll();


    Line findByLineName(String lineName);


    boolean existsByProject_ProjectId(Long updatedProjectId);
}
