package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CabinCrewRepository extends JpaRepository<CabinCrewEntity, Integer> {

    public Optional<CabinCrewEntity> findCabinCrewEntityByEmailAndPassword(String email, String password);
    public Optional<CabinCrewEntity> findByAttendantId(int attendantId);
    public Optional<CabinCrewEntity> findCabinCrewEntityByEmail(String email);
}
