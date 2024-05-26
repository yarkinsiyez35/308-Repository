package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentPK;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PilotAssignmentRepositoryTests {

    @Autowired
    private PilotAssignmentRepository pilotAssignmentRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PilotRepository pilotRepository;
    @Autowired
    private EntityManager entityManager;


    @AfterEach
    public void tearDown() {
        pilotAssignmentRepository.deleteAll();
    }

    /*
    @Test
    @Transactional
    public void PilotAssignmentRepository_FindAllByPilotAssignmentPK_PilotId()
    {
        PilotEntity pilotEntity = new PilotEntity("email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        FlightEntity flightEntity1 = new FlightEntity();
        FlightEntity flightEntity2 = new FlightEntity();
        flightEntity1.setFlightNumber("CS1000");
        flightEntity2.setFlightNumber("CS1030");

        flightEntity1 = flightRepository.save(flightEntity1);
        flightEntity2 = flightRepository.save(flightEntity2);


        pilotEntity = pilotRepository.save(pilotEntity);

        entityManager.flush();
        entityManager.clear();

        PilotAssignmentEntity pilotAssignmentEntity1 = new PilotAssignmentEntity(new PilotAssignmentPK(pilotEntity.getPilotId(), "CS1000"), "senior", "0a", 1);
        pilotAssignmentEntity1.setPilot(pilotEntity);
        pilotAssignmentEntity1.setFlight(flightEntity1);
        PilotAssignmentEntity pilotAssignmentEntity2 = new PilotAssignmentEntity(new PilotAssignmentPK(pilotEntity.getPilotId(), "CS1030"), "junior", "0a", 1);
        pilotAssignmentEntity2.setPilot(pilotEntity);
        pilotAssignmentEntity2.setFlight(flightEntity2);

        pilotAssignmentRepository.save(pilotAssignmentEntity1);
        pilotAssignmentRepository.save(pilotAssignmentEntity2);

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(1);

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(2);
    }

     */

    /*
    @Test
    @Transactional
    public void PilotAssignmentRepository_FindAllByPilotAssignmentPK_FlightNumber()
    {
        PilotEntity pilotEntity1 = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity pilotEntity2 = new PilotEntity(2,"emaill@gmail.com", "password","first", "sur", 30, "male", 5002, "Alien", "Junior");

        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("CS1000");

        flightRepository.save(flight);

        pilotRepository.save(pilotEntity1);
        pilotRepository.save(pilotEntity2);

        PilotAssignmentEntity pilotAssignmentEntity1 = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1000"), "senior", "0a", 1);
        PilotAssignmentEntity pilotAssignmentEntity2 = new PilotAssignmentEntity(new PilotAssignmentPK(2,"CS1000"), "senior", "0a", 1);

        pilotAssignmentEntity1.setFlight(flight);
        pilotAssignmentEntity1.setPilot(pilotEntity1);
        pilotAssignmentEntity2.setFlight(flight);
        pilotAssignmentEntity2.setPilot(pilotEntity2);


        pilotAssignmentRepository.save(pilotAssignmentEntity1);
        pilotAssignmentRepository.save(pilotAssignmentEntity2);

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber("CS1000");

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(2);


    }

     */

    /*
    @Test
    @Transactional
    public void PilotAssignmentRepository_DeletePilot()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("CS1000");

        flightRepository.save(flight);

        PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1000"), "senior", "0a", 1);

        pilotAssignmentEntity.setPilot(pilotEntity);
        pilotAssignmentEntity.setFlight(flight);

        pilotAssignmentRepository.save(pilotAssignmentEntity);

        pilotRepository.deleteById(1);


        entityManager.flush();  //force the database to reload
        entityManager.clear();

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAll();

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(1);
    }

     */
}
