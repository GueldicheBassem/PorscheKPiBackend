package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.dao.ProjectDataDao;
import com.Back.PorscheDashboardBackend.entity.ProjectData;
import com.Back.PorscheDashboardBackend.service.ProjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ProjectDataController {

    @Autowired
    private ProjectDataService projectDataService;
    @Autowired
    private ProjectDataDao projectDataDao;





    @GetMapping("/GetFileData")
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public List<ProjectData> getFileDataAndSave() {
        // Save data to database and retrieve the saved data
        List<ProjectData> savedDataList = projectDataService.saveAndReturnLatestDataForAllFiles();
        return savedDataList;
    }
}

