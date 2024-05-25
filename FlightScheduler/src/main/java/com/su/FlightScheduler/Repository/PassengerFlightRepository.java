package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//TESTING: this one function should be tested
public interface PassengerFlightRepository extends JpaRepository<PassengerFlight, Integer> {

    List<PassengerFlight> findPassengerFlightByFlight(FlightEntity flight);

    //List<PassengerFlight> findPassengerFlightByFlight(FlightEntity flight);
}
