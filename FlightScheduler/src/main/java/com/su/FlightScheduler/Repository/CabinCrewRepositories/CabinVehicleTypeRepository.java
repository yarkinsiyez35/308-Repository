package com.su.FlightScheduler.Repository.CabinCrewRepositories;

import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantVehicleTypeEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantVehicleTypePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CabinVehicleTypeRepository extends JpaRepository<AttendantVehicleTypeEntity, AttendantVehicleTypePK> {

    public List<AttendantVehicleTypeEntity> findAttendantVehicleTypeEntitiesByAttendantVehicleTypePK_AttendantId(int attendantId);
}
