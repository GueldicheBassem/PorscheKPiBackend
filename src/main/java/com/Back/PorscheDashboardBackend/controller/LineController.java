package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.dao.LineDao;
import com.Back.PorscheDashboardBackend.entity.Line;
import com.Back.PorscheDashboardBackend.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
@RequestMapping("/api/line")
public class LineController {

    @Autowired
    private LineService lineService;
    @Autowired
    private LineDao lineDao;

    @GetMapping
    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Line>> getAllLines() {
        List<Line> lines = lineService.getAllLines();
        return new ResponseEntity<>(lines, HttpStatus.OK);
    }



    @PostMapping
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Line> createNewLine(@RequestBody Line line) {
        Long projectId = line.getProject().getProjectId();
        boolean projectInUse = lineDao.existsByProject_ProjectId(projectId);

        if (projectInUse) {
            // Project already assigned to another line, return conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Line createdLine = lineService.createNewLine(line); // Assuming lineService creates the line
        return new ResponseEntity<>(createdLine, HttpStatus.CREATED);
    }

    @PutMapping("/{lineId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Line> updateLine(@PathVariable Long lineId, @RequestBody Line updatedLine) {
        Optional<Line> existingLineOptional = lineDao.findById(lineId);
        if (!existingLineOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Line existingLine = existingLineOptional.get();

        // Check if another line already references the updated project
        Long updatedProjectId = updatedLine.getProject().getProjectId();
        boolean projectInUse = lineDao.existsByProject_ProjectId(updatedProjectId);
        if (projectInUse && !updatedProjectId.equals(existingLine.getProject().getProjectId())) {
            // Project already assigned to another line, return conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Update the existing line with the new values
        existingLine.setLineName(updatedLine.getLineName());
        existingLine.setProject(updatedLine.getProject());

        // Save the updated line
        Line savedLine = lineDao.save(existingLine);

        return new ResponseEntity<>(savedLine, HttpStatus.OK);
    }




    @DeleteMapping("/{lineId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
