package com.Back.PorscheDashboardBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String projectName;
    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<Serial> serials; // Change to List to represent multiple Serials
    @JsonBackReference("project-line")
    @OneToOne(mappedBy = "project")
    private Line line;

    // Constructors, getters, and setters
}
