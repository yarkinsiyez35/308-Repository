package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.FlightEntitites.PlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, String> {

}