package com.Back.PorscheDashboardBackend.dao;

import com.Back.PorscheDashboardBackend.entity.Serial;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SerialDao extends CrudRepository<Serial, Long> {
    List<Serial> findByProject_Line_LineId(Long lineId);


    Serial findBySerialName(String serialName);


}
