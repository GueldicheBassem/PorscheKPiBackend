    package com.Back.PorscheDashboardBackend.entity;

    import lombok.Data;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import lombok.Setter;

    import java.io.Serializable;
    import java.sql.Date;
    @Getter
    @Setter
    @RequiredArgsConstructor
    public class ShiftId implements Serializable {

        private Date date;
        private String shift;

        // Constructors, getters, and setters
    }