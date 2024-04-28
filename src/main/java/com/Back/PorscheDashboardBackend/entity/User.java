package com.Back.PorscheDashboardBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    private String userName;
    private String userPassword;
    private String Name;
    private String Lastname;
    private Long Matricule;
    private int Penaltypoints;
        private String superiorEmail;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;
}

