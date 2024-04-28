package com.Back.PorscheDashboardBackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.sql.Date;
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@IdClass(ShiftId.class) // Define the composite primary key class
public class Shift implements Serializable {

    @Id
    private Date date; // Date of the shift

    @Id
    private String shift; // Shift type (Morning/Afternoon)


    private boolean finished; // Indicates whether the shift is finished

    // Constructors, getters, and setters
}
