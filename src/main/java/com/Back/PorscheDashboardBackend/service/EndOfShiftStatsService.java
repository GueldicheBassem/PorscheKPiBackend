    package com.Back.PorscheDashboardBackend.service;

    import com.Back.PorscheDashboardBackend.dao.EndOfShiftKosuDao;
    import com.Back.PorscheDashboardBackend.dao.EndOfShiftStatsDao;
    import com.Back.PorscheDashboardBackend.entity.EndOfShiftKosu;
    import com.Back.PorscheDashboardBackend.entity.EndOfShiftStats;
    import com.Back.PorscheDashboardBackend.entity.ProcessedEndOfShiftStats;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.sql.Date;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @Service
    public class EndOfShiftStatsService {

        @Autowired
        private EndOfShiftStatsDao endOfShiftStatsDao;
        @Autowired
        private EndOfShiftKosuDao endOfShiftKosuDao;
        public EndOfShiftStats saveEndOfShiftStats(EndOfShiftStats endOfShiftStats) {
            return endOfShiftStatsDao.save(endOfShiftStats);
        }
        public List<ProcessedEndOfShiftStats> getLast7DaysEfficiency() {
            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Get the date 7 days ago
            LocalDate sevenDaysAgo = currentDate.minusDays(7);

            // Create start and end LocalDateTime objects for the past 7 days
            Date startDate = Date.valueOf(sevenDaysAgo);
            Date endDate = Date.valueOf(currentDate);

            // Query the database for entries within the past 7 days based on Shift date
            List<EndOfShiftStats> endOfShiftStatsList = endOfShiftStatsDao.findByShiftDateBetween(startDate, endDate);

            // Process the data and populate ProcessedEndOfShiftStats objects
            Map<String, List<Double>> efficiencyMap = new HashMap<>();
            Map<String, String> shiftTypeMap = new HashMap<>();

            for (EndOfShiftStats stats : endOfShiftStatsList) {
                String shiftDateKey = stats.getShift().getDate().toString() + "_" + stats.getShift().getShift();

                // Calculate sum of efficiencies
                if (efficiencyMap.containsKey(shiftDateKey)) {
                    efficiencyMap.get(shiftDateKey).add(stats.getEfficiency());
                } else {
                    List<Double> efficiencies = new ArrayList<>();
                    efficiencies.add(stats.getEfficiency());
                    efficiencyMap.put(shiftDateKey, efficiencies);
                }

                // Populate shift type map
                if (!shiftTypeMap.containsKey(shiftDateKey)) {
                    shiftTypeMap.put(shiftDateKey, stats.getShift().getShift());
                }
            }

            // Create and populate ProcessedEndOfShiftStats objects
            List<ProcessedEndOfShiftStats> processedStatsList = new ArrayList<>();
            for (String key : efficiencyMap.keySet()) {
                ProcessedEndOfShiftStats processedStats = new ProcessedEndOfShiftStats();
                processedStats.setShiftDate(key.split("_")[0]);
                processedStats.setShiftType(shiftTypeMap.get(key));


                List<Double> efficiencies = efficiencyMap.get(key);
                double sum = efficiencies.stream().mapToDouble(Double::doubleValue).sum();
                double averageEfficiency = sum / efficiencies.size();

                // Calculate PPM
                double nokNumberSum = endOfShiftStatsList.stream()
                        .filter(stats -> (stats.getShift().getDate().toString() + "_" + stats.getShift().getShift()).equals(key))
                        .mapToDouble(EndOfShiftStats::getNokNumber).sum();
                double producedNumberSum = endOfShiftStatsList.stream()
                        .filter(stats -> (stats.getShift().getDate().toString() + "_" + stats.getShift().getShift()).equals(key))
                        .mapToDouble(EndOfShiftStats::getProducedNumber).sum();
                double ppm = (nokNumberSum / producedNumberSum) * 1_000_000; // Calculate PPM

                processedStats.setAverageEfficiency(averageEfficiency);
                processedStats.setPpm(ppm); // Set PPM

                processedStatsList.add(processedStats);
            }

            return processedStatsList;
        }

        // You can add more methods as needed for your business logic
    }
