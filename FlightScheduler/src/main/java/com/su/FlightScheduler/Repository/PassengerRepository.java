package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Integer> {

    public Optional<PassengerEntity> findPassengerEntityByEmailAndPassword(String email, String password);
}
