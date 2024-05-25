package com.su.FlightScheduler.Repository.PilotRepositories;

import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TESTING: these two functions should be tested
@Repository
public interface PilotAssignmentRepository extends JpaRepository<PilotAssignmentEntity, PilotAssignmentPK> {
    public List<PilotAssignmentEntity> findAllByPilotAssignmentPK_PilotId(int pilotId);
    public List<PilotAssignmentEntity> findAllByPilotAssignmentPK_FlightNumber(String flightNumber);

}
