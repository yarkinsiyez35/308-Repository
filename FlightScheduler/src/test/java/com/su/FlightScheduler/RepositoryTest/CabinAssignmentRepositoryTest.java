package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsPK;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinAssignmentRepositoryTest {

    @Autowired
    private CabinAssignmentRepository cabinAssignmentRepository;

    @Autowired
    private CabinCrewRepository cabinCrewRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void CabinAssignmentRepository_FindAllByCabinCrewAssignmentsPK_AttendantId() {
        FlightEntity flight1 = new FlightEntity("CS1034", "Flight CS1034 Info");
        FlightEntity flight2 = new FlightEntity("CS1503", "Flight CS1503 Info");

        flightRepository.save(flight1);
        flightRepository.save(flight2);

        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity(1, "John", "Cena", "john.cena@gmail.com", "password325", 32, "Male", "USA", "Senior");
        cabinCrewRepository.save(cabinCrewEntity1);

        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity1 = new CabinCrewAssignmentsEntity(new CabinCrewAssignmentsPK(1, "FL100"), "senior", "12A", 1);
        cabinCrewAssignmentsEntity1.setCabinCrew(cabinCrewEntity1); // Set the cabin crew reference
        cabinCrewAssignmentsEntity1.setFlight(flight1); // Set the flight reference

        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity2 = new CabinCrewAssignmentsEntity(new CabinCrewAssignmentsPK(1, "FL101"), "junior", "14B", 1);
        cabinCrewAssignmentsEntity2.setCabinCrew(cabinCrewEntity1); // Set the cabin crew reference
        cabinCrewAssignmentsEntity2.setFlight(flight2); // Set the flight reference

        cabinAssignmentRepository.save(cabinCrewAssignmentsEntity1);
        cabinAssignmentRepository.save(cabinCrewAssignmentsEntity2);

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(1);

        Assertions.assertThat(cabinCrewAssignmentsEntityList.size()).isEqualTo(2);
    }

    @Test
    public void CabinAssignmentRepository_FindAllByCabinCrewAssignmentsPK_FlightNumber() {
        FlightEntity flight1 = new FlightEntity("CS1034", "Flight 1034 Info");
        flightRepository.save(flight1);

        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity(1, "John", "Cena", "john.cena@gmail.com", "password325", 32, "Male", "USA", "Senior");
        CabinCrewEntity cabinCrewEntity2 = new CabinCrewEntity(2, "Jane", "Brand", "jane.brand@gmail.com", "password564", 28, "Female", "Canada", "Junior");

        cabinCrewRepository.save(cabinCrewEntity1);
        cabinCrewRepository.save(cabinCrewEntity2);

        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity1 = new CabinCrewAssignmentsEntity(new CabinCrewAssignmentsPK(1, "FL100"), "senior", "12A", 1);
        cabinCrewAssignmentsEntity1.setCabinCrew(cabinCrewEntity1); // Set the cabin crew reference
        cabinCrewAssignmentsEntity1.setFlight(flight1); // Set the flight reference

        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity2 = new CabinCrewAssignmentsEntity(new CabinCrewAssignmentsPK(2, "FL100"), "junior", "14B", 1);
        cabinCrewAssignmentsEntity2.setCabinCrew(cabinCrewEntity2); // Set the cabin crew reference
        cabinCrewAssignmentsEntity2.setFlight(flight1); // Set the flight reference

        cabinAssignmentRepository.save(cabinCrewAssignmentsEntity1);
        cabinAssignmentRepository.save(cabinCrewAssignmentsEntity2);

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber("FL100");

        Assertions.assertThat(cabinCrewAssignmentsEntityList.size()).isEqualTo(2);
    }


}