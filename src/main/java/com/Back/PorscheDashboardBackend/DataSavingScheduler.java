package com.Back.PorscheDashboardBackend;

import com.Back.PorscheDashboardBackend.entity.ProjectData;
import com.Back.PorscheDashboardBackend.service.ProjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataSavingScheduler {
@Autowired
    ProjectDataService projectDataService;







    public void saveDataPeriodically() {
        System.out.println("HELLO ITS RUNNING");
        // Save data to database and retrieve the saved data
        projectDataService.saveAndReturnLatestDataForAllFiles();
        // Optionally, you can log or handle the saved data here
    }
}
