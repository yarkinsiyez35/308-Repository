package com.su.FlightScheduler.Entity.CabinCrewEntites;

import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cabin_crew_assignments")
public class CabinCrewAssignmentsEntity implements Serializable {

    @EmbeddedId
    private CabinCrewAssignmentsPK cabinCrewAssignmentsPK;

    @Column(name = "assignment_role")
    private String assignmentRole;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "accepted_assignment")
    private int acceptedAssignment;

    @ManyToOne
    @MapsId("flightNumber")
    @JoinColumn(name = "flight_number")
    private FlightEntity flight;


    @ManyToOne
    @MapsId("attendantId")
    @JoinColumn(name = "attendant_id")
    private CabinCrewEntity cabinCrew;

    public CabinCrewAssignmentsEntity() {
    }

    public CabinCrewAssignmentsEntity(CabinCrewAssignmentsPK cabinCrewAssignmentsPK, String assignmentRole, String seatNumber, int acceptedAssignment) {
        this.cabinCrewAssignmentsPK = cabinCrewAssignmentsPK;
        this.assignmentRole = assignmentRole;
        this.seatNumber = seatNumber;
        this.acceptedAssignment = acceptedAssignment;
    }

    public CabinCrewAssignmentsPK getCabinCrewAssignmentsPK() {
        return cabinCrewAssignmentsPK;
    }

    public String getAssignmentRole() {
        return assignmentRole;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getAcceptedAssignment() {
        return acceptedAssignment;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public CabinCrewEntity getCabinCrew() {
        return cabinCrew;
    }

    public void setCabinCrewAssignmentsPK(CabinCrewAssignmentsPK cabinCrewAssignmentsPK) {
        this.cabinCrewAssignmentsPK = cabinCrewAssignmentsPK;
    }

    public void setAssignmentRole(String assignmentRole) {
        this.assignmentRole = assignmentRole;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setAcceptedAssignment(int acceptedAssignment) {
        this.acceptedAssignment = acceptedAssignment;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public void setCabinCrew(CabinCrewEntity cabinCrew) {
        this.cabinCrew = cabinCrew;
    }
}
