package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, String> {
    public Optional<AirportEntity> findByCode(String code);
}