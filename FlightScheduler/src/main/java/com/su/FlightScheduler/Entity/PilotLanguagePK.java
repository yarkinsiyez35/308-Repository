package com.su.FlightScheduler.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PilotLanguagePK implements Serializable {
    @Column(name = "pilot_id", nullable = false)
    private int pilotId;

    @Column(name = "language", nullable = false)
    private String language;

    public PilotLanguagePK() {
    }

    public PilotLanguagePK(int pilotId, String language) {
        this.pilotId = pilotId;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PilotLanguagePK that = (PilotLanguagePK) o;
        return pilotId == that.pilotId && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pilotId, language);
    }

    public int getPilotId() {
        return pilotId;
    }

    public String getLanguage() {
        return language;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
