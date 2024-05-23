package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;

import java.util.List;

public interface AttendantAssignmentService {

    public UserDataDTO getFlightsOfAttendant(int attendantId);   //return the flights of a given pilot with id

    public UserDataDTO assignAttendantToFlight(String flightNumber, int attendantId); //assign a pilot to a flight

    public List<UserDataDTO> getAttendantsOfFlight(String flightNumber);    //get all pilots in a flight
}
