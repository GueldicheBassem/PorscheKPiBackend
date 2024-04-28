package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.EndOfShiftKosu;
import com.Back.PorscheDashboardBackend.entity.Shift;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface EndOfShiftKosuDao extends CrudRepository<EndOfShiftKosu, Long> {
    List<EndOfShiftKosu> findByShift(Shift existingShift);

    List<EndOfShiftKosu> findByShiftDateBetween(Date sevenDaysAgo, Date currentDate);
    // You can define additional methods here if needed
}
