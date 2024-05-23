package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.VehicleTypeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface FlightService {
    FlightEntity saveFlightObj(FlightEntity flight);
    FlightEntity createFlightFilled(String flightNumber, String flightInfo, AirportEntity
            sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange,
                                    LocalDateTime departureDateTime,
                                    LocalDateTime landingDateTime, boolean sharedFlight,
                                    CompanyEntity sharedFlightCompany, AdminEntity admin,
                                    String standardMenu);
    FlightEntity createFlight(String flightNumber, String flightInfo, AdminEntity admin);
    FlightEntity addFlightParams1(String flightNumber, PlaneEntity plane, AirportEntity sourceAirport, AirportEntity destinationAirport);
    FlightEntity addFlightParams2(String flightNumber, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime);
    FlightEntity addFlightParams3(String flightNumber, boolean sharedFlight, CompanyEntity sharedFlightCompany);

    // --- Find Methods ---
    Optional<FlightEntity> findFlightByNumber(String flightNumber);
    List<FlightEntity> findAllFlights();
    List<FlightEntity> findFlightsByDepartureAirport(String airportCode);
    List<FlightEntity> findFlightsByDestinationAirport(String airportCode);
    List<FlightEntity> findFlightsByDepartureAndDestinationAirport(String departureAirportCode, String destinationAirportCode);
    List<FlightEntity> findFlightsByDepartureDateTime(LocalDateTime departureDateTime);
    List<FlightEntity> findFlightsByLandingDateTime(LocalDateTime landingDateTime);
    List<FlightEntity> findFlightsByDepartureAndLandingDateTime(LocalDateTime departureDateTime, LocalDateTime landingDateTime);
    List<FlightEntity> findFlightsByDepartureAirportAndDepartureDateTime(String airportCode, LocalDateTime departureDateTime);
    List<FlightEntity> findFlightsByDestinationAirportAndLandingDateTime(String airportCode, LocalDateTime landingDateTime);
    List<FlightEntity> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime);
    List<FlightEntity> findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime);
    // --- End of Find Methods ---

    void deleteFlightByNumber(String flightNumber);


    // --- Update Methods ---
    FlightEntity updateFlight(FlightEntity flight);
    FlightEntity updateFlightInfo(String flightNumber, String flightInfo);
    FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport);
    FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport);
    FlightEntity updatePlane(String flightNumber, PlaneEntity plane);
    FlightEntity updateFlightRange(String flightNumber, Integer flightRange);
    FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime);
    FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime);
    FlightEntity updateSharedFlight(String flightNumber, boolean sharedFlight);
    FlightEntity updateSharedFlightCompany(String flightNumber, CompanyEntity sharedFlightCompany);
    FlightEntity updateStandardMenu(String flightNumber, String standardMenu);
    // --- End of Update Methods ---


    // --- Getters for the entities ---
    AirportEntity getSourceAirport(String flightNumber);
    AirportEntity getDestinationAirport(String flightNumber);
    PlaneEntity getPlane(String flightNumber);
    CompanyEntity getCompany(String flightNumber);
    LocalDateTime getDateTime(String flightNumber);
    // --- End of Getters for the entities ---



    AirportEntity getAirportFromRequest(Map<String, Object> request, String key);
    PlaneEntity getPlaneFromRequest(Map<String, Object> request, String key);
    CompanyEntity getCompanyFromRequest(Map<String, Object> request, String key);
    LocalDateTime getDateTimeFromRequest(Map<String, Object> request, String key);

    // --- Getters for DTOs (Projections) ---
    VehicleTypeRepository.SeatingPlanProjection findSeatingPlanByFlightNumber(String flightNumber);
    VehicleTypeRepository.AttendeeCapacityProjection findAttendeeCapacityByFlightNumber(String flightNumber);
    VehicleTypeRepository.PilotCapacityProjection findPilotCapacityByFlightNumber(String flightNumber);
    // --- End of Getters for DTOs (Projections) ---

    List<SeatingTypeDTO> decodeSeatingPlan(String flightNumber);
    List<SeatingDTO> findBookedFlightsByFlightNumber(String flightNumber);
}
