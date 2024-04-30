package com.su.FlightScheduler.DTO;

import com.su.FlightScheduler.Entity.PilotEntity;




// DTO will be used for Flight Roster Information
public class PilotSimplifiedDTO {
    private int pilotId;
    private String first_name;
    private String surname;
    private String seniority;

    public PilotSimplifiedDTO()
    { }

    public PilotSimplifiedDTO(PilotEntity pilotEntity)
    {
        this.pilotId = pilotEntity.getPilotId();
        this.first_name = pilotEntity.getFirstName();
        this.surname = pilotEntity.getSurname();
        this.seniority = pilotEntity.getSeniority();
    }

    public int getPilotId() {
        return pilotId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSeniority() {
        return seniority;
    }
}
