package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//TESTING: these 2 functions should be tested
public interface PassengerRepository extends JpaRepository<PassengerEntity, Integer> {

    public Optional<PassengerEntity> findPassengerEntityByEmailAndPassword(String email, String password);
    public Optional<PassengerEntity> findPassengerEntityByEmail(String email);
}
