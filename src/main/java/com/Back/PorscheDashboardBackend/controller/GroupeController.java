package com.Back.PorscheDashboardBackend.controller;

import com.Back.PorscheDashboardBackend.entity.Groupe;
import com.Back.PorscheDashboardBackend.entity.Groupe;
import com.Back.PorscheDashboardBackend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupeController {

    @Autowired
    private GroupService groupService;

    @GetMapping

    @PreAuthorize("hasAnyRole('AdminUser', 'KosuUser','NormalUser')")
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        List<Groupe> groups = groupService.getAllGroupes();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Groupe> createGroupe(@RequestBody Groupe group) {
        Groupe createdGroupe = groupService.createNewGroup(group);
        return new ResponseEntity<>(createdGroupe, HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Groupe> updateGroupe(@PathVariable Long groupId, @RequestBody Groupe group) {
        Groupe updatedGroupe = groupService.updateGroupe(groupId, group);
        return new ResponseEntity<>(updatedGroupe, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('AdminUser')")
    public ResponseEntity<Void> deleteGroupe(@PathVariable Long groupId) {
        groupService.deleteGroupe(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
