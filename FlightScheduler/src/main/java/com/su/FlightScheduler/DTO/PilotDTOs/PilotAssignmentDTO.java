package com.su.FlightScheduler.DTO.PilotDTOs;


import com.su.FlightScheduler.Entity.PilotAssignmentEntity;

//this class is used to hold information about a pilot's assignment
//this class will be used in PilotFullDTO as a List
public class PilotAssignmentDTO {
    private String seatNumber;
    private int acceptedAssignment;
    private String flightNumber;        //this part will be converted to FlightInfoForPilotDTO

    public PilotAssignmentDTO() {
    }

    public PilotAssignmentDTO(String seatNumber, int acceptedAssignment, String flightNumber) {
        this.seatNumber = seatNumber;
        this.acceptedAssignment = acceptedAssignment;
        this.flightNumber = flightNumber;
    }

    public PilotAssignmentDTO(PilotAssignmentEntity pilotAssignmentEntity)
    {
        this.seatNumber = pilotAssignmentEntity.getSeatNumber();;
        this.acceptedAssignment = pilotAssignmentEntity.getAcceptedAssignment();
        this.flightNumber = pilotAssignmentEntity.getPilotAssignmentPK().getFlightNumber();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getAcceptedAssignment() {
        return acceptedAssignment;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setAcceptedAssignment(int acceptedAssignment) {
        this.acceptedAssignment = acceptedAssignment;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}

