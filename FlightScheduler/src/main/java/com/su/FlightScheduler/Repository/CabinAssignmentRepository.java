package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewAssignmentsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CabinAssignmentRepository extends JpaRepository<CabinCrewAssignmentsEntity, CabinCrewAssignmentsPK> {

    public List<CabinCrewAssignmentsEntity> findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(int attendantId);
}
