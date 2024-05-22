package com.su.FlightScheduler.Entity.CabinCrewEntites;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "attendant_languages") // Adjust table name as necessary
public class AttendantLanguageEntity implements Serializable {

    @EmbeddedId
    private AttendantLanguagePK attendantLanguagePK;

    public AttendantLanguageEntity() {
    }

    public AttendantLanguageEntity(AttendantLanguagePK attendantLanguagePK) {
        this.attendantLanguagePK = attendantLanguagePK;
    }

    public AttendantLanguagePK getAttendantLanguagePK() {
        return attendantLanguagePK;
    }

    public void setAttendantLanguagePK(AttendantLanguagePK attendantLanguagePK) {
        this.attendantLanguagePK = attendantLanguagePK;
    }
}
