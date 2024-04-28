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
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"shift_date", "shift_shift","line_id", "hour", "serial_id"})
})
public class HourlyKosu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "shift_date", referencedColumnName = "date"),
            @JoinColumn(name = "shift_shift", referencedColumnName = "shift")
    })
    private Shift shift;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serial_id")
    private Serial serial;

    private int hour;
    private int workforce;
    private int netWorkTime;
    private int producedCables;
    private int nokCables;

    private double mudas;

    private double KosuEquivalent;

    // Constructors, getters, and setters
}
