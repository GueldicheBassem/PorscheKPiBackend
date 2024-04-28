package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.SerialDao;
import com.Back.PorscheDashboardBackend.entity.Line;
import com.Back.PorscheDashboardBackend.entity.Serial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerialService {

    @Autowired
    private SerialDao serialDao;

    public Serial createNewSerial(Serial serial){
        return serialDao.save(serial);
    }

    public Optional<Serial> getSerialById(Long serialId) {
        return serialDao.findById(serialId);
    }

    public Serial updateSerial(Serial serial) {
        return serialDao.save(serial);
    }

    public void deleteSerial(Long serialId) {
        serialDao.deleteById(serialId);
    }

    public List<Serial> getAllSerials() {
        return (List<Serial>) serialDao.findAll();
    }
}
