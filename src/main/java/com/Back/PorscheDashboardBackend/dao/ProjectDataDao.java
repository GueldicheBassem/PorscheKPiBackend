package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.ProjectData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectDataDao  extends CrudRepository<ProjectData,Long> {
    ProjectData findTopByOrderByDateDesc();

    ProjectData findTopByProjectOrderByDateDesc(String project);

    ProjectData findByProject(String project);

    List<ProjectData> findByDateContaining(Date date);

    List<ProjectData> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}

