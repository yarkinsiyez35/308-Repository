package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
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

    @Test
    public void PilotRepository_FindBySeniorityAndAllowedRangeGreaterThanEqual() {
        PilotEntity seniorPilot = new PilotEntity(0, "senior@gmail.com", "password", "first name", "surname", 40, "male", 7000, "Alien", "Senior");
        PilotEntity juniorPilot = new PilotEntity(0, "junior@gmail.com", "password", "first name", "surname", 25, "female", 3000, "Alien", "Junior");
        pilotRepository.save(seniorPilot);
        pilotRepository.save(juniorPilot);

        List<PilotEntity> seniorPilots = pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual("Senior", 5000);

        Assertions.assertThat(seniorPilots).isNotEmpty();
        Assertions.assertThat(seniorPilots.size()).isEqualTo(1);
        Assertions.assertThat(seniorPilots.get(0).getEmail()).isEqualTo("senior@gmail.com");
        Assertions.assertThat(seniorPilots.get(0).getAllowedRange()).isGreaterThanOrEqualTo(5000);
    }



}
