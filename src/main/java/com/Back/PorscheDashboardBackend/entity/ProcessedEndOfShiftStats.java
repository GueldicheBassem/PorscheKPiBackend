package com.Back.PorscheDashboardBackend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessedEndOfShiftStats {
    private String shiftDate;
    private String shiftType;
    private double averageEfficiency;
    private double ppm; // Parts Per Million


}
