package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.VehicleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleTypeEntity, String> {
    <T> T findByVehicleType(String vehicleType, Class<T> type);

    interface PilotCapacityProjection {
        int getSeniorPilotCapacity();
        int getJuniorPilotCapacity();
        int getTraineePilotCapacity();
    }

    interface AttendeeCapacityProjection {
        int getSeniorAttendeeCapacity();
        int getJuniorAttendeeCapacity();
        int getChefAttendeeCapacity();
    }

    interface SeatingPlanProjection {
        String getSeatingPlan();
    }
}
