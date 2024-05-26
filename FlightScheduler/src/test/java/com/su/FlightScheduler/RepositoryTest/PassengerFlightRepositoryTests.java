package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PassengerFlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import com.su.FlightScheduler.Repository.PassengerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PassengerFlightRepositoryTests {

    @Autowired
    private PassengerFlightRepository passengerFlightRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    public void PassengerFlightRepository_FindPassengerFlightByFlight() {
        // Create FlightEntity
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber("FN123");
        flightEntity.setDepartureDateTime(LocalDateTime.now());
        flightEntity.setSourceAirport(null);
        flightEntity.setDestinationAirport(null);
        flightEntity.setPlane(null);
        flightEntity.setFlightRange(1000);

        // Save FlightEntity first
        flightEntity = flightRepository.save(flightEntity);

        // Create PassengerEntity
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        passengerEntity.setEmail("email@gmail.com");
        passengerEntity.setPassword("password");
        passengerEntity.setFirstName("John"); // Set the first name
        passengerEntity.setSurname("Doe"); // Set the surname
        passengerEntity.setGender("Male"); // Set the gender
        passengerEntity.setNationality("USA");

        // Save PassengerEntity
        passengerEntity = passengerRepository.save(passengerEntity);

        // Create PassengerFlight
        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setFlight(flightEntity);
        passengerFlight.setPassenger(passengerEntity);
        passengerFlight.setBookingId(1);
        passengerFlight.setIsParent("F");
        passengerFlight.setSeatNumber("1A");

        // Save PassengerFlight
        passengerFlightRepository.save(passengerFlight);

        // Find PassengerFlight by FlightEntity
        List<PassengerFlight> passengerFlights = passengerFlightRepository.findPassengerFlightByFlight(flightEntity);

        // Assertions
        Assertions.assertThat(passengerFlights).isNotEmpty();
        Assertions.assertThat(passengerFlights.size()).isEqualTo(1);
        Assertions.assertThat(passengerFlights.get(0).getFlight().getFlightNumber()).isEqualTo("FN123");
    }


}

