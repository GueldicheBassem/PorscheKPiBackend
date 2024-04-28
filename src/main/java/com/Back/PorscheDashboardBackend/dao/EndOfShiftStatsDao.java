package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.EndOfShiftStats;
import com.Back.PorscheDashboardBackend.entity.Shift;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface EndOfShiftStatsDao extends CrudRepository<EndOfShiftStats, Long> {
    List<EndOfShiftStats> findByShift(Shift existingShift);


    List<EndOfShiftStats> findByShiftDateBetween(Date localDate, Date localDate1);
    // You can add custom query methods here if needed
}
