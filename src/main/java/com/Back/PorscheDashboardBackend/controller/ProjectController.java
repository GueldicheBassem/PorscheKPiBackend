package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.dao.ProjectDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.Back.PorscheDashboardBackend.entity.Project;
import com.Back.PorscheDashboardBackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@RestController
@RequestMapping("/api/projects")
@PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createNewProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(projectId, project);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}