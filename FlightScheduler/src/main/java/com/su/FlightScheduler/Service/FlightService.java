package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.PlaneEntity;
import com.su.FlightScheduler.Repository.VehicleTypeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface FlightService {

    FlightEntity getFlightOrThrow(String flightId);
    FlightEntity createFlight(FlightDataDTO flightDataDTO, int adminId);
    FlightEntity saveFlightObj(FlightEntity flight);
    FlightEntity createFlightFilled(String flightNumber, String flightInfo, AirportEntity
            sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange,
                                    LocalDateTime departureDateTime,
                                    LocalDateTime landingDateTime, boolean sharedFlight,
                                    CompanyEntity sharedFlightCompany, AdminEntity admin,
                                    String standardMenu);


    // --- Find Methods ---
    Optional<FlightEntity> findFlightByNumber(String flightNumber);
    FlightDataDTO findFlightByNumberDTO(String flightNumber);
    List<FlightEntity> findAllFlights();
    List<FlightDataDTO> findAllFlightsDTO();
    List<FlightDataDTO> findFlightsByDepartureAirport(String airportCode);
    List<FlightDataDTO> findFlightsByDestinationAirport(String airportCode);
    List<FlightDataDTO> findFlightsByDepartureAndDestinationAirport(String departureAirportCode, String destinationAirportCode);
    List<FlightDataDTO> findFlightsByDepartureDateTime(LocalDateTime departureDateTime);
    List<FlightDataDTO> findFlightsByLandingDateTime(LocalDateTime landingDateTime);
    List<FlightDataDTO> findFlightsByDepartureAndLandingDateTime(LocalDateTime departureDateTime, LocalDateTime landingDateTime);
    List<FlightDataDTO> findFlightsByDepartureAirportAndDepartureDateTime(String airportCode, LocalDateTime departureDateTime);
    List<FlightDataDTO> findFlightsByDestinationAirportAndLandingDateTime(String airportCode, LocalDateTime landingDateTime);
    List<FlightDataDTO> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime);

    // --- End of Find Methods ---

    void deleteFlightByNumber(String flightNumber);


    // --- Update Methods ---
    FlightEntity updateFlight(FlightDataDTO flight, int adminID);
    FlightEntity updateFlightByFlightObject(FlightEntity flight);
    FlightEntity updateFlightByFlightDTO(FlightDataDTO flight, int adminId);
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


    // --- Getters for DTOs (Projections) ---
    VehicleTypeRepository.SeatingPlanProjection findSeatingPlanByFlightNumber(String flightNumber);
    VehicleTypeRepository.AttendeeCapacityProjection findAttendeeCapacityByFlightNumber(String flightNumber);
    VehicleTypeRepository.PilotCapacityProjection findPilotCapacityByFlightNumber(String flightNumber);
    // --- End of Getters for DTOs (Projections) ---

    List<SeatingTypeDTO> decodeSeatingPlan(String flightNumber);
    List<SeatingDTO> findBookedFlightsByFlightNumber(String flightNumber);
    List<UserDataDTO> getUsersDTOByFlightNumber(String flightNumber);
    List<String> getAvailableSeats(String flightNumber);
}
