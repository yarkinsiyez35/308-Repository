package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.AttendantVehicleTypeEntity;
import com.su.FlightScheduler.Entity.AttendantVehicleTypePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CabinVehicleTypeRepository extends JpaRepository<AttendantVehicleTypeEntity, AttendantVehicleTypePK> {

    public List<AttendantVehicleTypeEntity> findAttendantVehicleTypeEntitiesByAttendantVehicleTypePK_AttendantId(int attendantId);
}
