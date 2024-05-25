package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.VehicleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//TESTING: these 11 functions should be tested
@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, String> {
    @Query("SELECT f.plane.vehicleType FROM FlightEntity f WHERE f.flightNumber = :flight_number")
    VehicleTypeEntity findVehicleTypeByFlightId(@Param("flight_number") String flight_number);

    @Query("SELECT f.flightNumber as flightNumber, f.sourceAirport as sourceAirport, f.destinationAirport as destinationAirport, f.departureDateTime as departureDateTime, f.landingDateTime as landingDateTime FROM FlightEntity f WHERE f.flightNumber = :flightNumber")
    FlightDetailsProjection findFlightDetailsByFlightNumber(String flightNumber);

    List<FlightEntity> findBySourceAirportAirportCode(String airportCode);

    List<FlightEntity> findByDestinationAirportAirportCode(String airportCode);

    List<FlightEntity> findBySourceAirportAirportCodeAndDestinationAirportAirportCode(String departureAirportCode, String destinationAirportCode);

    List<FlightEntity> findByDepartureDateTime(LocalDateTime departureDateTime);

    List<FlightEntity> findByLandingDateTime(LocalDateTime landingDateTime);

    List<FlightEntity> findByDepartureDateTimeAndLandingDateTime(LocalDateTime departureDateTime, LocalDateTime landingDateTime);

    List<FlightEntity> findBySourceAirportAirportCodeAndDepartureDateTime(String airportCode, LocalDateTime departureDateTime);

    List<FlightEntity> findByDestinationAirportAirportCodeAndLandingDateTime(String airportCode, LocalDateTime landingDateTime);

    List<FlightEntity> findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime);


    interface FlightDetailsProjection {
        String getFlightNumber();
        AirportEntity getSourceAirport();
        AirportEntity getDestinationAirport();
        LocalDateTime getDepartureDateTime();
        LocalDateTime getLandingDateTime();
    }
}