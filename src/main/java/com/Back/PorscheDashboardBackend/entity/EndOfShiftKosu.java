package com.Back.PorscheDashboardBackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class EndOfShiftKosu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "shift_date", referencedColumnName = "date"),
            @JoinColumn(name = "shift_shift", referencedColumnName = "shift")
    })
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "serial_id")
    private Serial serial;

    @ManyToOne
    @JoinColumn(name="Groupe_Id")
    private Groupe groupe;

    private int totalNetWorkTime;
    private int totalProducedCables;
    private int totalNokCables;
    private int Workforce;
    private double totalMudas;
    private double averageKosuEquivalent;

    // Constructors, getters, and setters
}
