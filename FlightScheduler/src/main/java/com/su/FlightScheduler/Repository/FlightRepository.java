package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.VehicleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, String> {
    @Query("SELECT f.plane.vehicleType FROM FlightEntity f WHERE f.flightNumber = :flight_number")
    VehicleTypeEntity findVehicleTypeByFlightId(@Param("flight_number") String flight_number);
}