package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.PilotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PilotRepositoryTests {

    @Autowired
    private PilotRepository pilotRepository;

    @Test
    public void PilotRepository_SavePilot()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);

        Assertions.assertThat(savedPilotEntity).isNotNull();
        Assertions.assertThat(savedPilotEntity.getPilotId()).isEqualTo(1);
    }

    @Test
    public void PilotRepository_FindPilotById()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);

        PilotEntity foundPilotEntity = pilotRepository.findById(1).get();

        Assertions.assertThat(foundPilotEntity).isNotNull();
        Assertions.assertThat(foundPilotEntity.getPilotId()).isEqualTo(1);
    }

    @Test
    public void PilotRepository_UpdatePilot()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);
        savedPilotEntity.setAge(45);

        PilotEntity updatedPilotEntity = pilotRepository.save(savedPilotEntity);

        Assertions.assertThat(updatedPilotEntity).isNotNull();
        Assertions.assertThat(updatedPilotEntity.getAge()).isEqualTo(45);
    }

    @Test
    public void PilotRepository_DeletePilot()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        pilotRepository.deleteById(1);

        Optional<PilotEntity> deletedPilot = pilotRepository.findById(1);

        Assertions.assertThat(deletedPilot.isEmpty()).isEqualTo(true);
    }


    @Test
    public void PilotRepository_FindByEmail()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        Optional<PilotEntity> pilot = pilotRepository.findPilotEntityByEmail("email@gmail.com");

        Assertions.assertThat(pilot.isPresent()).isEqualTo(true);
    }

    @Test
    public void PilotRepository_FindByEmailAndPassWord()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        Optional<PilotEntity> pilot = pilotRepository.findPilotEntityByEmailAndPassword("email@gmail.com", "password");

        Assertions.assertThat(pilot.isPresent()).isEqualTo(true);
    }




    @Test
    public void PilotRepository_SaveAll()
    {
        PilotEntity pilotEntity1 = new PilotEntity(1,"email1@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity pilotEntity2 = new PilotEntity(2,"email2@gmail.com", "password","first", "last", 29, "female", 5001, "Alien", "Junior");

        List<PilotEntity> pilotEntityList = new ArrayList<>();
        pilotEntityList.add(pilotEntity1);
        pilotEntityList.add(pilotEntity2);

        List<PilotEntity> savedPilotEntityList = pilotRepository.saveAll(pilotEntityList);

        Assertions.assertThat(savedPilotEntityList).isNotNull();
        Assertions.assertThat(savedPilotEntityList.size()).isEqualTo(2);
    }

    @Test
    public void PilotRepository_FindAll()
    {
        PilotEntity pilotEntity1 = new PilotEntity(1,"email1@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity pilotEntity2 = new PilotEntity(2,"email2@gmail.com", "password","first", "last", 29, "female", 5001, "Alien", "Junior");

        List<PilotEntity> pilotEntityList = new ArrayList<>();
        pilotEntityList.add(pilotEntity1);
        pilotEntityList.add(pilotEntity2);

        pilotRepository.saveAll(pilotEntityList);

        List<PilotEntity> savedPilotEntityList = pilotRepository.findAll();

        Assertions.assertThat(savedPilotEntityList).isNotNull();
        Assertions.assertThat(savedPilotEntityList.size()).isEqualTo(2);
    }

}
