package com.Back.PorscheDashboardBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Serial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;
    private String serialName;
    private String reference;
    private double coefficient;

    @ManyToOne
    private Project project;

    // Constructors, getters, and setters
}
