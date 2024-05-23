package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PilotAssignmentPK implements Serializable {

    @Column(name = "pilot_id")
    private Integer pilotId;

    @Column(name = "flight_number")
    private String flightNumber;


    public PilotAssignmentPK() {
    }

    public PilotAssignmentPK(Integer pilotId, String flightNumber) {
        this.pilotId = pilotId;
        this.flightNumber = flightNumber;
    }

    public int getPilotId() {
        return pilotId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PilotAssignmentPK that = (PilotAssignmentPK) o;
        return pilotId == that.pilotId && Objects.equals(flightNumber, that.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pilotId, flightNumber);
    }
}
