package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinCrewRepository extends JpaRepository<CabinCrewEntity, Integer> {
}
