package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Repository.PassengerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PassengerRepositoryTests {

    @Autowired
    private PassengerRepository passengerRepository;

    @AfterEach
    void tearDown() {
        passengerRepository.deleteAll();
    }

    @Test
    public void PassengerRepository_FindByEmail()
    {
        PassengerEntity passengerEntity = new PassengerEntity(
                78,
                "email@gmail.com",
                "password",
                "ismet",
                "ayvaz",
                22,
                "male",
                "Turkish"
        );
        passengerRepository.save(passengerEntity);

        Optional<PassengerEntity> passenger = passengerRepository.findPassengerEntityByEmail("email@gmail.com");

        Assertions.assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @Test
    public void PassengerRepository_FindByEmailAndPassWord()
    {
        PassengerEntity passengerEntity = new PassengerEntity(
                78,
                "email@gmail.com",
                "password",
                "ismet",
                "ayvaz",
                22,
                "male",
                "Turkish"
        );
        passengerRepository.save(passengerEntity);

        Optional<PassengerEntity> passenger = passengerRepository.findPassengerEntityByEmailAndPassword("email@gmail.com", "password");

        Assertions.assertThat(passenger.isPresent()).isEqualTo(true);
    }
}
