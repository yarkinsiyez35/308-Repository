package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    FlightEntity saveFlight(FlightEntity flight, AdminEntity admin);
    FlightEntity createFlight(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime, boolean sharedFlight, CompanyEntity sharedFlightCompany, AdminEntity admin, String standardMenu);
    FlightEntity createFlight(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, AdminEntity admin);
    FlightEntity createFlight(String flightNumber, String flightInfo, AdminEntity admin);
    Optional<FlightEntity> findFlightByNumber(String flightNumber, AdminEntity admin);
    List<FlightEntity> findAllFlights(AdminEntity admin);
    FlightEntity updateFlight(FlightEntity flight, AdminEntity admin);
    void deleteFlightByNumber(String flightNumber, AdminEntity admin);
    FlightEntity updateFlightInfo(String flightNumber, String flightInfo, AdminEntity admin);
    FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport, AdminEntity admin);
    FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport, AdminEntity admin);
    FlightEntity updatePlane(String flightNumber, PlaneEntity plane, AdminEntity admin);
    FlightEntity updateFlightRange(String flightNumber, Integer flightRange, AdminEntity admin);
    FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime, AdminEntity admin);
    FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime, AdminEntity admin);
    FlightEntity updateSharedFlight(String flightNumber, boolean sharedFlight, AdminEntity admin);
    FlightEntity updateSharedFlightCompany(String flightNumber, CompanyEntity sharedFlightCompany, AdminEntity admin);
    /*FlightEntity updateAdmin(String flightNumber, AdminEntity admin);*/
    FlightEntity updateStandardMenu(String flightNumber, String standardMenu, AdminEntity admin);
}