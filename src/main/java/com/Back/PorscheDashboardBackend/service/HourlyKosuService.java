package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.HourlyKosuDao;
import com.Back.PorscheDashboardBackend.entity.HourlyKosu;
import com.Back.PorscheDashboardBackend.entity.HourlyKosuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HourlyKosuService {

    @Autowired
    private HourlyKosuDao hourlyKosuDao;

    public HourlyKosu createHourlyKosu(HourlyKosu hourlyKosu) {
        // Calculate kosuEquivalent
       

        // Save entity
        return hourlyKosuDao.save(hourlyKosu);
    }
    public List<HourlyKosuResponse> mapHourlyKosuToResponse(List<HourlyKosu> hourlyKosuEntries) {
        List<HourlyKosuResponse> responses = new ArrayList<>();
        for (HourlyKosu hourlyKosu : hourlyKosuEntries) {
            HourlyKosuResponse response = new HourlyKosuResponse();
            response.setEntryId(hourlyKosu.getEntryId());
            response.setGroup(hourlyKosu.getGroupe() != null ? hourlyKosu.getGroupe().getGroupName() : null);
            response.setLine(hourlyKosu.getLine() != null ? hourlyKosu.getLine().getLineName() : null);
            response.setHour(hourlyKosu.getHour());
            response.setSerial(hourlyKosu.getSerial() != null ? hourlyKosu.getSerial().getSerialName() : null);
            response.setNetWorkTime(hourlyKosu.getNetWorkTime());
            response.setProducedCables(hourlyKosu.getProducedCables());
            response.setNokCables(hourlyKosu.getNokCables());
            response.setWorkforce(hourlyKosu.getWorkforce());
            response.setEquivalentKosu(hourlyKosu.getKosuEquivalent());
            response.setMudas(hourlyKosu.getMudas());
            responses.add(response);
        }
        return responses;
    }


    // You can add more methods here as needed
}
