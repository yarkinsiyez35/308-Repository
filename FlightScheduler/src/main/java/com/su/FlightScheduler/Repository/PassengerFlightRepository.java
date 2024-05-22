package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerFlightRepository extends JpaRepository<PassengerFlight, Integer> {

    List<PassengerFlight> findPassengerFlightByFlight(FlightEntity flight);
}
