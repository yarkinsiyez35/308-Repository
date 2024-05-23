package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.VehicleTypeEntity;
import com.su.FlightScheduler.Repository.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class VehicleTypeRepositoryTests {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Test
    void testSaveAndFindById() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Boeing7371");
        vehicleType.setBusinessCapacity(12);
        vehicleType.setEconomyCapacity(150);
        vehicleType.setSeniorPilotCapacity(2);
        vehicleType.setJuniorPilotCapacity(4);
        vehicleType.setTraineePilotCapacity(2);
        vehicleType.setSeniorAttendeeCapacity(4);
        vehicleType.setJuniorAttendeeCapacity(8);
        vehicleType.setChefAttendeeCapacity(1);
        vehicleType.setSeatingPlan("3-3");

        vehicleTypeRepository.save(vehicleType);

        Optional<VehicleTypeEntity> found = vehicleTypeRepository.findById("Boeing7371");
        assertTrue(found.isPresent());
        assertEquals("Boeing7371", found.get().getVehicleType());
    }

    @Test
    void testFindByVehicleType() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("AirbusA3201");
        vehicleTypeRepository.save(vehicleType);

        VehicleTypeEntity found = vehicleTypeRepository.findByVehicleType("AirbusA3201", VehicleTypeEntity.class);
        assertNotNull(found);
        assertEquals("AirbusA3201", found.getVehicleType());
    }

    @Test
    void testPilotCapacityProjection() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Boeing7471");
        vehicleType.setSeniorPilotCapacity(4);
        vehicleType.setJuniorPilotCapacity(8);
        vehicleType.setTraineePilotCapacity(2);
        vehicleTypeRepository.save(vehicleType);

        VehicleTypeRepository.PilotCapacityProjection projection = vehicleTypeRepository.findByVehicleType("Boeing7471", VehicleTypeRepository.PilotCapacityProjection.class);
        assertNotNull(projection);
        assertEquals(4, projection.getSeniorPilotCapacity());
        assertEquals(8, projection.getJuniorPilotCapacity());
        assertEquals(2, projection.getTraineePilotCapacity());
    }

    @Test
    void testAttendeeCapacityProjection() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("AirbusA3801");
        vehicleType.setSeniorAttendeeCapacity(6);
        vehicleType.setJuniorAttendeeCapacity(10);
        vehicleType.setChefAttendeeCapacity(2);
        vehicleTypeRepository.save(vehicleType);

        VehicleTypeRepository.AttendeeCapacityProjection projection = vehicleTypeRepository.findByVehicleType("AirbusA3801", VehicleTypeRepository.AttendeeCapacityProjection.class);
        assertNotNull(projection);
        assertEquals(6, projection.getSeniorAttendeeCapacity());
        assertEquals(10, projection.getJuniorAttendeeCapacity());
        assertEquals(2, projection.getChefAttendeeCapacity());
    }

    @Test
    void testSeatingPlanProjection() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Concorde1");
        vehicleType.setSeatingPlan("2-2");
        vehicleTypeRepository.save(vehicleType);

        VehicleTypeRepository.SeatingPlanProjection projection = vehicleTypeRepository.findByVehicleType("Concorde1", VehicleTypeRepository.SeatingPlanProjection.class);
        assertNotNull(projection);
        assertEquals("2-2", projection.getSeatingPlan());
    }

    @Test
    void testDelete() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Embraer1901");
        vehicleTypeRepository.save(vehicleType);

        vehicleTypeRepository.deleteById("Embraer1901");

        Optional<VehicleTypeEntity> found = vehicleTypeRepository.findById("Embraer1901");
        assertFalse(found.isPresent());
    }
}
