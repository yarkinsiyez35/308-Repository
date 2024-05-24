package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.List;

public interface PilotFlightAssignmentService
{
    public UserDataDTO getFlightsOfPilot(int pilotId);   //return the flights of a given pilot with id

    public List<UserDataDTO> getAvailablePilotsForFlight(String flightNumber);  //return the list of available pilots for a given flight
    public UserDataDTO assignPilotToFlight(String flightNumber, int pilotId); //assign a pilot to a flight
    public UserDataDTO assignAPilotToFlightWithGivenRoleAndSeat(String flightNumber, String role, String seatNumber) throws RuntimeException;
    public List<UserDataDTO> getPilotsOfFlight(String flightNumber);    //get all pilots in a flight
    public UserDataDTO removeFlightFromAPilot(String flightNumber, int pilotId);    //removes a pilot from a flight

}
