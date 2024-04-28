package com.Back.PorscheDashboardBackend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProjectData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String project;
    private int workforcePresent;
    private double efficiency;
    private int producedNumber;

    private double rft;
    private int nokNumber;
    private LocalDateTime date;

    public double getPpm() {
        return ppm;
    }

    public void setPpm(double ppm) {
        this.ppm = ppm;
    }

    private double ppm;

    public Project getProjectName() {
        return projectName;
    }

    public void setProjectName(Project projectName) {
        this.projectName = projectName;
    }

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private Project projectName;


    public ProjectData() {
    }

    public ProjectData(String project, int workforcePresent, double efficiency, int producedNumber, int nokNumber, LocalDateTime date, double rft) {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getWorkforcePresent() {
        return workforcePresent;
    }

    public void setWorkforcePresent(int workforcePresent) {
        this.workforcePresent = workforcePresent;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public int getProducedNumber() {
        return producedNumber;
    }

    public void setProducedNumber(int producedNumber) {
        this.producedNumber = producedNumber;
    }

    public int getNokNumber() {
        return nokNumber;
    }

    public void setNokNumber(int nokNumber) {
        this.nokNumber = nokNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    // Getters and setters for rft
    public double getRft() {
        return rft;
    }

    public void setRft(double rft) {
        this.rft = rft;
    }

    // Getters and setters
}
