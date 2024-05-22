package com.su.FlightScheduler.Entity.CabinCrewEntites;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

//I made the flight number string to add it to list in the DTO
@Embeddable
public class CabinCrewAssignmentsPK implements Serializable {

    @Column(name = "attendant_id", nullable = false)
    private int attendantId;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    public CabinCrewAssignmentsPK() {
    }

    public CabinCrewAssignmentsPK(int attendantId, String flightNumber) {
        this.attendantId = attendantId;
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CabinCrewAssignmentsPK that = (CabinCrewAssignmentsPK) o;
        return attendantId == that.attendantId && Objects.equals(flightNumber, that.flightNumber);
    }

    @Override
    public int hashCode() {return Objects.hash(attendantId, flightNumber);}


    public int getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}
