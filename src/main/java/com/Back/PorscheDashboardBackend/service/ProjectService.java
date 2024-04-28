package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.ProjectDao;
import com.Back.PorscheDashboardBackend.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    public List<Project> getAllProjects() {
        return (List<Project>) projectDao.findAll();
    }

    public Project createNewProject(Project project) {
        return projectDao.save(project);
    }

    public Project updateProject(Long projectId, Project project) {
        Optional<Project> existingProjectOptional = projectDao.findById(projectId);
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();
            // Update properties of existingProject with values from project
            existingProject.setProjectName(project.getProjectName());
            return projectDao.save(existingProject);
        } else {
            // Handle not found scenario
            return null;
        }
    }

    public void deleteProject(Long projectId) {
        projectDao.deleteById(projectId);
    }
}
