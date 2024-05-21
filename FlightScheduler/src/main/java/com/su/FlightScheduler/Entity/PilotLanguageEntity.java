package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "pilot_languages")
public class PilotLanguageEntity implements Serializable {

    @EmbeddedId
    private PilotLanguagePK pilotLanguagePK;
    public PilotLanguageEntity() {
    }

    public PilotLanguageEntity(PilotLanguagePK pilotLanguagePK) {
        this.pilotLanguagePK = pilotLanguagePK;
    }

    public PilotLanguagePK getPilotLanguagePK() {
        return pilotLanguagePK;
    }

    public void setPilotLanguagePK(PilotLanguagePK pilotLanguagePK) {
        this.pilotLanguagePK = pilotLanguagePK;
    }
}
