package com.su.FlightScheduler.DTO.FrontEndDTOs;


import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;

import java.time.LocalDateTime;

//this class is used to hold information about a pilot's assignment
//this class will be used in PilotFullDTO as a List
public class UserFlightDataDTO {
    private FlightDataDTO flightData;   //this part is used for all
    private String userSeat;    //this part will be converted to SeatDTO in future
    private LocalDateTime boughtTime;   //this part is used only for passengers
    private Integer purchaseId;     //this part is used only for passengers
    private String role;    //this part is used for pilot and cabin crew


    public UserFlightDataDTO() {
    }

    public UserFlightDataDTO(FlightDataDTO flightData, String userSeat, LocalDateTime boughtTime, int purchaseId, String role) {
        this.flightData = flightData;
        this.userSeat = userSeat;
        this.boughtTime = boughtTime;
        this.purchaseId = purchaseId;
        this.role = role;
    }

    public UserFlightDataDTO(FlightEntity flightEntity, PilotAssignmentEntity pilotAssignmentEntity)
    {
        this.flightData = new FlightDataDTO(flightEntity);
        this.userSeat = pilotAssignmentEntity.getSeatNumber();
        this.boughtTime = null;
        this.purchaseId = null;
        this.role = pilotAssignmentEntity.getAssignmentRole();
    }


    //add for CabinCrewAssignments Entity
    //add for PassengerFlight


    public FlightDataDTO getFlightData() {
        return flightData;
    }

    public String getUserSeat() {
        return userSeat;
    }

    public LocalDateTime getBoughtTime() {
        return boughtTime;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public String getRole() {
        return role;
    }

    public void setFlightData(FlightDataDTO flightData) {
        this.flightData = flightData;
    }

    public void setUserSeat(String userSeat) {
        this.userSeat = userSeat;
    }

    public void setBoughtTime(LocalDateTime boughtTime) {
        this.boughtTime = boughtTime;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

