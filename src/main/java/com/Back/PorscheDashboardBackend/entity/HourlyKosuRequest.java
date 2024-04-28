    package com.Back.PorscheDashboardBackend.entity;

    import lombok.Data;

    @Data
    public class HourlyKosuRequest {
        private long entryId;
        private ShiftId shiftId;
        private Long lineId;
        private Long serialId;
        private int netWorkTime;
        private int producedCables;
        private int nokCables;
        private int workforce;
        private int hour;
        private Long groupId;

        // Constructors, getters, and setters
    }
