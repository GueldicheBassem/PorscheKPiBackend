package com.Back.PorscheDashboardBackend.service;

import com.Back.PorscheDashboardBackend.dao.LineDao;
import com.Back.PorscheDashboardBackend.entity.Line;
import com.Back.PorscheDashboardBackend.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {

    @Autowired
    private LineDao lineDao;

    public Line createNewLine(Line line){
        return lineDao.save(line);
    }

    public List<Line> getAllLines() {
        return (List<Line>) lineDao.findAll();
    }

    public void deleteLine(Long lineId) {
        lineDao.deleteById(lineId);
    }

    // You can add more methods here as needed
}
