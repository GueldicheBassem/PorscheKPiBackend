package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.ShiftDao;
import com.Back.PorscheDashboardBackend.entity.Shift;
import com.Back.PorscheDashboardBackend.entity.ShiftId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {

    @Autowired
    private ShiftDao shiftDao;

    public Shift createNewShift(Shift shift) {
        return shiftDao.save(shift);
    }

    // You can add more methods here as needed
}
