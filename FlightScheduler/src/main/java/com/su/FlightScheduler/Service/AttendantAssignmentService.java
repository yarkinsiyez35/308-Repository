package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;

import java.util.List;

public interface AttendantAssignmentService {

    public UserDataDTO getFlightsOfAttendant(int attendantId);   //return the flights of a given pilot with id
    public List<UserDataDTO> getAvailableAttendantsForFlight(String flightNumber);
    public UserDataDTO assignAttendantToFlight(String flightNumber, int attendantId); //assign a pilot to a flight
    public UserDataDTO assignAttendantToFlightWithGivenRoleAndSeat(String flightNumber, String role, String seatNumber) throws RuntimeException;
    public List<UserDataDTO> getAttendantsOfFlight(String flightNumber);    //get all pilots in a flight
    public UserDataDTO removeAttendantFromFlight(String flightNumber, int attendantId);    //removes a pilot from a flight
}
