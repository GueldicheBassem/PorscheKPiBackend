package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.EndOfShiftKosuDao;
import com.Back.PorscheDashboardBackend.entity.EndOfShiftKosu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EndOfShiftKosuService {

    @Autowired
    private EndOfShiftKosuDao endOfShiftKosuDao;

    public EndOfShiftKosu createEndOfShiftKosu(EndOfShiftKosu endOfShiftKosu) {
        return endOfShiftKosuDao.save(endOfShiftKosu);
    }

    public List<EndOfShiftKosu> getLast7DaysKosu() {
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysAgo = currentDate.minusDays(4);
        Date startDate = Date.valueOf(sevenDaysAgo);
        Date endDate = Date.valueOf(currentDate);

        List<EndOfShiftKosu> endOfShiftKosuList = endOfShiftKosuDao.findByShiftDateBetween(startDate, endDate);

        return endOfShiftKosuList;
    }

    public Map<String, Map<String, Map<String, Double>>> processKosuData(List<EndOfShiftKosu> endOfShiftKosuList) {
        Map<String, Map<String, Map<String, Double>>> kosuMap = new HashMap<>();
        for (EndOfShiftKosu kosu : endOfShiftKosuList) {
            if (kosu.getLine() == null || kosu.getLine().getLineName() == null ||
                    kosu.getSerial() == null || kosu.getSerial().getSerialName() == null ||
                    kosu.getGroupe() == null || kosu.getGroupe().getGroupName() == null ){
                // Skip this entry if any of the required fields are null
                continue;
            }
            String shiftDateKey = kosu.getShift().getDate().toString();
            String shiftType = kosu.getShift().getShift();
            String lineName = kosu.getLine().getLineName();
            String projectName = kosu.getLine().getProject().getProjectName(); // Fetch the project name associated with the line
            Double kosuEquivalent = kosu.getAverageKosuEquivalent();

            kosuMap.putIfAbsent(shiftDateKey, new HashMap<>());
            kosuMap.get(shiftDateKey).putIfAbsent(shiftType, new HashMap<>());
            kosuMap.get(shiftDateKey).get(shiftType).put(projectName, kosuEquivalent); // Use project name instead of line name
        }
        return kosuMap;
    }




}
