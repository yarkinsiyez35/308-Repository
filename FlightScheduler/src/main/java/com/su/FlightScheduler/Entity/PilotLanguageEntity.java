package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pilot_languages")
public class PilotLanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pilot_id", referencedColumnName = "pilot_id")
    private PilotEntity pilot;

    @Column(name = "language")
    private String language;


}
