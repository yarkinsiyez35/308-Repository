package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.Entity.PassengerFlight;

import java.util.List;

public interface PassengerFlightService {

    public PassengerFlight bookFlight(int passengerId, String flightNumber, String isParent, String seatNumber);
    //public List<PassengerFlight> findBookedFlightsByFlightNumber(String flightNumber);
    public PassengerFlight findBookingById(int id);
    public PassengerFlight cancelFlight(int id);
    public List<PassengerFlightDTO> findAllBookings();
    public UserDataDTO findBookedFlightsByPassengerId(int passengerId);
}
