package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.List;

public interface PilotFlightAssignmentService
{
    public UserDataDTO getFlightsOfPilot(int pilotId);   //return the flights of a given pilot with id

    public UserDataDTO assignPilotToFlight(String flightNumber, int pilotId); //assign a pilot to a flight

    public List<UserDataDTO> getPilotsOfFlight(String flightNumber);    //get all pilots in a flight

}
