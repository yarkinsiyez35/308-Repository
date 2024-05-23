package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.AirportEntity;
import com.su.FlightScheduler.Repository.AirportRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AirportRepositoryTests {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    public void AirportRepository_FindByAirportCode() {
        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setAirportCode("ABC");
        // set other properties of airportEntity as needed

        airportRepository.save(airportEntity);

        Optional<AirportEntity> airport = airportRepository.findAirportEntityByAirportCode("ABC");

        Assertions.assertThat(airport.isPresent()).isEqualTo(true);
        Assertions.assertThat(airport.get().getAirportCode()).isEqualTo("ABC");
    }
}