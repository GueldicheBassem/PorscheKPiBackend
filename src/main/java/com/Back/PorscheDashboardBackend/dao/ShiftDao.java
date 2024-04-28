package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Shift;
import com.Back.PorscheDashboardBackend.entity.ShiftId;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ShiftDao extends CrudRepository<Shift, ShiftId> {
    Shift findByDateAndShift(Date date, String shift);

    Shift findByDateAndShift(java.util.Date date, String shift);
}
