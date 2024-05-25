package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//TESTING: this one function should be tested
@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, String> {
    public Optional<AirportEntity> findAirportEntityByAirportCode(String code);
}