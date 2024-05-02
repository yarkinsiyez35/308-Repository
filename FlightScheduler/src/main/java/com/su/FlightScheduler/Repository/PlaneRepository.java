package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.PlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, String> {
    public Optional<PlaneEntity> findByCode(String code);
}