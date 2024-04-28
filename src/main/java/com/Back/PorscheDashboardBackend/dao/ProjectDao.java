package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectDao extends CrudRepository<Project, Long> {
    Project findByProjectName(String project);

    }

