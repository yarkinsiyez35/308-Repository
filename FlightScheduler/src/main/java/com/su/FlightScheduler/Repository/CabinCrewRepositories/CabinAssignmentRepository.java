package com.su.FlightScheduler.Repository.CabinCrewRepositories;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CabinAssignmentRepository extends JpaRepository<CabinCrewAssignmentsEntity, CabinCrewAssignmentsPK> {

    public List<CabinCrewAssignmentsEntity> findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(int attendantId);
    public List<CabinCrewAssignmentsEntity> findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(String flightNumber);
}
