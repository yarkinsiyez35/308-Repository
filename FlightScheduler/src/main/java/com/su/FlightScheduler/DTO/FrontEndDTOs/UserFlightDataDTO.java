package com.su.FlightScheduler.DTO.FrontEndDTOs;


import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Service.FlightService;
import com.su.FlightScheduler.Service.FlightServiceImp;
import com.su.FlightScheduler.Util.SeatTypeFinder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//this class is used to hold information about a pilot's assignment
//this class will be used in PilotFullDTO as a List
public class UserFlightDataDTO {
    private FlightDataDTO flightData;   //this part is used for all
    private SeatingDTO userSeat;    //this part will be converted to SeatingDTO in future
    private LocalDateTime boughtTime;   //this part is used only for passengers
    private Integer purchaseId;     //this part is used only for passengers
    private String role;    //this part is used for pilot and cabin crew

    public UserFlightDataDTO() {
    }

    public UserFlightDataDTO(FlightDataDTO flightData, SeatingDTO userSeat, LocalDateTime boughtTime, Integer purchaseId, String role) {
        this.flightData = flightData;
        this.userSeat = userSeat;
        this.boughtTime = boughtTime;
        this.purchaseId = purchaseId;
        this.role = role;
    }

    public UserFlightDataDTO(FlightEntity flightEntity, PilotAssignmentEntity pilotAssignmentEntity)
    {
        this.flightData = new FlightDataDTO(flightEntity);
        SeatingDTO userSeat = new SeatingDTO();
        userSeat.setSeatPosition(pilotAssignmentEntity.getSeatNumber());
        userSeat.setSeatType("Pilot");
        userSeat.setStatus(true);
        userSeat.setUserId(pilotAssignmentEntity.getPilot().getPilotId());
        this.userSeat = userSeat;
        this.boughtTime = null;
        this.purchaseId = null;
        this.role = pilotAssignmentEntity.getAssignmentRole();
    }

    public UserFlightDataDTO(FlightEntity flightEntity, CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity)
    {
        this.flightData = new FlightDataDTO(flightEntity);
        SeatingDTO userSeat = new SeatingDTO();
        userSeat.setSeatPosition(cabinCrewAssignmentsEntity.getSeatNumber());
        userSeat.setSeatType("Cabin crew");
        userSeat.setStatus(true);
        userSeat.setUserId(cabinCrewAssignmentsEntity.getCabinCrew().getAttendantId());
        this.userSeat = userSeat;
        this.boughtTime = null;
        this.purchaseId = null;
        this.role = cabinCrewAssignmentsEntity.getAssignmentRole();
    }


    public UserFlightDataDTO(FlightEntity flightEntity, PassengerFlight passengerFlight){
        this.flightData = new FlightDataDTO(flightEntity);
        SeatingDTO userSeat = new SeatingDTO();
        userSeat.setSeatPosition(passengerFlight.getSeatNumber());

        //String seatType = SeatTypeFinder.getSeatType(passengerFlight.getSeatNumber(), flightEntity.getPlane().getVehicleType().getSeatingPlan());
        //String seatType = FlightService.
        String seatType = "economy";
        if (Objects.equals(passengerFlight.getIsParent(), "T"))
        {
            userSeat.setSeatType(seatType + " with child");
        }
        else
        {
            userSeat.setSeatType(seatType);
        }


        userSeat.setStatus(true);
        userSeat.setUserId(passengerFlight.getPassenger().getPassengerId());
        this.userSeat = userSeat;
        this.boughtTime = null; // might change
        this.purchaseId = passengerFlight.getBookingId();
        this.role = null;
    }




    public FlightDataDTO getFlightData() {
        return flightData;
    }

    public SeatingDTO getUserSeat() {
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

    public void setUserSeat(SeatingDTO userSeat) {
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

