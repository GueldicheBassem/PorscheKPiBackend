package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.ProjectDao;
import com.Back.PorscheDashboardBackend.dao.ProjectDataDao;
import com.Back.PorscheDashboardBackend.entity.Project;
import com.Back.PorscheDashboardBackend.entity.ProjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectDataService {

    @Autowired
    private ProjectDataDao projectDataDao;
@Autowired
private ProjectDao projectDao;

    private String[] filePaths = {
            "C:\\MainDirectory\\Education\\Developpement\\Efficience_T295.csv",
            "C:\\MainDirectory\\Education\\Developpement\\Efficience_T280.csv",
            "C:\\MainDirectory\\Education\\Developpement\\Efficience_T273.csv",
            "C:\\MainDirectory\\Education\\Developpement\\Efficience_T728.csv"
    };
    private LocalDateTime[] lastModifiedTimes = new LocalDateTime[4];

    @Scheduled(fixedDelay = 5000)
    public void saveDataPeriodically() {

        saveAndReturnLatestDataForAllFiles();

    }


    private LocalDateTime getLastModifiedTime(String filePath) throws IOException {
        File file = new File(filePath);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());
    }

    private ProjectData saveLatestEntryFromFile(String  filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String lastLine = null;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }

            if (lastLine != null) {
                ProjectData latestData = parseProjectDataFromLine(lastLine);
                latestData.setPpm(calculatePPM(latestData.getNokNumber(), latestData.getProducedNumber())); // Calculate and set PPM

                // Fetch the Project entity from the database based on the project name
                Project project = projectDao.findByProjectName(latestData.getProject());
                latestData.setProjectName(project); // Set the fetched Project entity

                ProjectData existingData = projectDataDao.findTopByProjectOrderByDateDesc(latestData.getProject());
                if (existingData != null && isSameData(existingData, latestData)) {
                    // If data is the same, update existing entry
                    updateProjectData(existingData, latestData);
                    return projectDataDao.save(existingData);
                } else {
                    // Check if data exists for the project
                    ProjectData projectData = projectDataDao.findByProject(latestData.getProject());
                    if (projectData != null) {
                        // If data exists, update the existing entry
                        updateProjectData(projectData, latestData);
                        return projectDataDao.save(projectData);
                    } else {
                        // Save new entry
                        return projectDataDao.save(latestData);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
        return null; // Return null if no data was saved
    }

    private boolean isSameData(ProjectData data1, ProjectData data2) {
        return data1.getProject().equals(data2.getProject())
                && data1.getWorkforcePresent() == data2.getWorkforcePresent()
                && data1.getEfficiency() == data2.getEfficiency()
                && data1.getProducedNumber() == data2.getProducedNumber()
                && data1.getNokNumber() == data2.getNokNumber()
                && data1.getDate().equals(data2.getDate())
                && data1.getRft()==(data2.getRft());
    }


    private void updateProjectData(ProjectData existingData, ProjectData newData) {
        existingData.setWorkforcePresent(newData.getWorkforcePresent());
        existingData.setEfficiency(newData.getEfficiency());
        existingData.setProducedNumber(newData.getProducedNumber());
        existingData.setNokNumber(newData.getNokNumber());
        existingData.setRft(newData.getRft());
        existingData.setDate(newData.getDate());
        existingData.setPpm(calculatePPM(newData.getNokNumber(), newData.getProducedNumber()));
    }

    private ProjectData parseProjectDataFromLine(String line) {
        String[] data = line.split(";");
        ProjectData p = new ProjectData();
        p.setProject(data[0]);
        p.setWorkforcePresent(Integer.parseInt(data[1]));
        p.setEfficiency(Double.parseDouble(data[2].replace(",", ".")));
        p.setProducedNumber(Integer.parseInt(data[3]));
        p.setNokNumber(Integer.parseInt(data[4]));
        String dateString = data[5].trim();
        LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd:MM:yyyy / HH:mm:ss"));
        p.setDate(date);
        p.setRft(Double.parseDouble(data[6].replace(",", ".")));
        return p;
    }



    public List<ProjectData> saveAndReturnLatestDataForAllFiles() {
        List<ProjectData> savedDataList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ProjectData latestData = saveLatestEntryFromFile(filePaths[i]);
            if (latestData != null) {
                savedDataList.add(latestData);
            }
        }
        return savedDataList;
    }

    private double calculatePPM(int nokNumber, int producedNumber) {
        if (producedNumber == 0) {
            return 0; // Avoid division by zero
        }
        return (double) nokNumber / producedNumber * 1_000_000;
    }
}

