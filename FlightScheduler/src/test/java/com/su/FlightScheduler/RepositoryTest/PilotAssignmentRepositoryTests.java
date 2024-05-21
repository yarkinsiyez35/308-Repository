package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentPK;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
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
public class PilotAssignmentRepositoryTests {

    @Autowired
    private PilotAssignmentRepository pilotAssignmentRepository;
    @Autowired
    private PilotRepository pilotRepository;
    @Autowired
    private EntityManager entityManager;


    @Test
    public void PilotAssignmentRepository_FindAllByPilotAssignmentPK_PilotId()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        PilotAssignmentEntity pilotAssignmentEntity1 = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1000"), "senior", "0a", 1);
        PilotAssignmentEntity pilotAssignmentEntity2 = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1030"), "junior", "0a", 1);

        pilotAssignmentRepository.save(pilotAssignmentEntity1);
        pilotAssignmentRepository.save(pilotAssignmentEntity2);

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(1);

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(2);
    }

    @Test
    public void PilotAssignmentRepository_FindAllByPilotAssignmentPK_FlightNumber()
    {
        PilotEntity pilotEntity1 = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity pilotEntity2 = new PilotEntity(2,"emaill@gmail.com", "password","first", "sur", 30, "male", 5002, "Alien", "Junior");

        pilotRepository.save(pilotEntity1);
        pilotRepository.save(pilotEntity2);

        PilotAssignmentEntity pilotAssignmentEntity1 = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1000"), "senior", "0a", 1);
        PilotAssignmentEntity pilotAssignmentEntity2 = new PilotAssignmentEntity(new PilotAssignmentPK(2,"CS1000"), "senior", "0a", 1);

        pilotAssignmentRepository.save(pilotAssignmentEntity1);
        pilotAssignmentRepository.save(pilotAssignmentEntity2);

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber("CS1000");

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(2);
    }

    @Test
    public void PilotAssignmentRepository_DeletePilot()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(new PilotAssignmentPK(1,"CS1000"), "senior", "0a", 1);

        pilotAssignmentRepository.save(pilotAssignmentEntity);

        pilotRepository.deleteById(1);


        entityManager.flush();  //force the database to reload
        entityManager.clear();

        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAll();

        Assertions.assertThat(pilotAssignmentEntityList.size()).isEqualTo(1);
    }
}
