package com.su.FlightScheduler.Repository.PilotRepositories;
import com.su.FlightScheduler.Entity.PilotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PilotRepository extends JpaRepository<PilotEntity, Integer> {

    public Optional<PilotEntity> findPilotEntityByEmailAndPassword(String email, String password);

    public Optional<PilotEntity> findPilotEntityByEmail(String email);

    public List<PilotEntity> findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual(String seniority, int range);

}
