package com.Back.PorscheDashboardBackend.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HourlyKosuResponse {
    private long entryId;
    private String group;
    private String line;
    private int hour;
    private String serial;
    private int netWorkTime;
    private int producedCables;
    private int nokCables;
    private int workforce;
    private double equivalentKosu;
    private double mudas;

    // Getters and setters
}


