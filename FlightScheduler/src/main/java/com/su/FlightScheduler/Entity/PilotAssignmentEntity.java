package com.su.FlightScheduler.Entity;


import jakarta.persistence.*;

@Entity
@Table(name= "pilot_assignments")
public class PilotAssignmentEntity {

    @EmbeddedId
    private PilotAssignmentPK pilotAssignmentPK;
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
    @MapsId("pilotId")
    @JoinColumn(name = "pilot_id")
    private PilotEntity pilot;

    public PilotAssignmentEntity() {
    }

    public PilotAssignmentEntity(PilotAssignmentPK pilotAssignmentPK, String assignmentRole, String seatNumber, int acceptedAssignment) {
        this.pilotAssignmentPK = pilotAssignmentPK;
        this.assignmentRole = assignmentRole;
        this.seatNumber = seatNumber;
        this.acceptedAssignment = acceptedAssignment;
    }

    public PilotAssignmentPK getPilotAssignmentPK() {
        return pilotAssignmentPK;
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

    public PilotEntity getPilot() {
        return pilot;
    }

    public void setPilotAssignmentPK(PilotAssignmentPK pilotAssignmentPK) {
        this.pilotAssignmentPK = pilotAssignmentPK;
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
}
