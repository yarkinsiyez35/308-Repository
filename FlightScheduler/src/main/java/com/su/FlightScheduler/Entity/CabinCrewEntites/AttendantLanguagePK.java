package com.su.FlightScheduler.Entity.CabinCrewEntites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AttendantLanguagePK implements Serializable {
    @Column(name = "attendant_id", nullable = false)
    private int attendantId;

    @Column(name = "language", nullable = false)
    private String language;

    public AttendantLanguagePK() {
    }

    public AttendantLanguagePK(int attendantId, String language) {
        this.attendantId = attendantId;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendantLanguagePK that = (AttendantLanguagePK) o;
        return attendantId == that.attendantId && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendantId, language);
    }

    public int getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

