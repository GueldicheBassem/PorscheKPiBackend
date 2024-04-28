package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.HourlyKosu;
import com.Back.PorscheDashboardBackend.entity.Shift;
import com.Back.PorscheDashboardBackend.entity.ShiftId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HourlyKosuDao extends CrudRepository<HourlyKosu, Long> {

    @Query("SELECT h FROM HourlyKosu h WHERE h.shift = :shift")
    List<HourlyKosu> getHourlyKosuFromShift(Shift shift);
    // You can define additional methods here if needed

    Optional<HourlyKosu> findByEntryId(Long entryId);

    HourlyKosu findTopByOrderByHourDesc();
}





