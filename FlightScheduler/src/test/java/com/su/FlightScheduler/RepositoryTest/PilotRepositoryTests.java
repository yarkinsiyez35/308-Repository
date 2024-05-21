package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PilotRepositoryTests {

    @Autowired
    private PilotRepository pilotRepository;

    @Test
    public void PilotRepository_FindByEmail()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        Optional<PilotEntity> pilot = pilotRepository.findPilotEntityByEmail("email@gmail.com");

        Assertions.assertThat(pilot.isPresent()).isEqualTo(true);
    }

    @Test
    public void PilotRepository_FindByEmailAndPassword()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        pilotRepository.save(pilotEntity);

        Optional<PilotEntity> pilot = pilotRepository.findPilotEntityByEmailAndPassword("email@gmail.com", "password");

        Assertions.assertThat(pilot.isPresent()).isEqualTo(true);
    }

}
