package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.PlaneEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.VehicleTypeEntity;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PlaneRepository;
import com.su.FlightScheduler.Repository.AirportRepository;
import com.su.FlightScheduler.Repository.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FlightRepositoryTests {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Test
    void testSaveAndFindById() {
        FlightEntity flight = createSampleFlightEntity("FN123");
        flightRepository.save(flight);

        Optional<FlightEntity> found = flightRepository.findById("FN123");
        assertTrue(found.isPresent());
        assertEquals("FN123", found.get().getFlightNumber());
    }

    @Test
    void testFindVehicleTypeByFlightId() {
        FlightEntity flight = createSampleFlightEntity("FN124");
        flightRepository.save(flight);

        VehicleTypeEntity vehicleType = flightRepository.findVehicleTypeByFlightId("FN124");
        assertNotNull(vehicleType);
        assertEquals("Boeing737", vehicleType.getVehicleType());
    }

    @Test
    void testFindFlightDetailsByFlightNumber() {
        FlightEntity flight = createSampleFlightEntity("FN125");
        flightRepository.save(flight);

        FlightRepository.FlightDetailsProjection details = flightRepository.findFlightDetailsByFlightNumber("FN125");
        assertNotNull(details);
        assertEquals("FN125", details.getFlightNumber());
        assertEquals("JFK", details.getSourceAirport().getAirportCode());
        assertEquals("LAX", details.getDestinationAirport().getAirportCode());
    }

    @Test
    void testFindBySourceAirportAirportCode() {
        FlightEntity flight = createSampleFlightEntity("FN126");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCode("JFK");
        assertFalse(flights.isEmpty());
        assertEquals("JFK", flights.get(0).getSourceAirport().getAirportCode());
    }

    @Test
    void testFindByDestinationAirportAirportCode() {
        FlightEntity flight = createSampleFlightEntity("FN127");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCode("LAX");
        assertFalse(flights.isEmpty());
        assertEquals("LAX", flights.get(0).getDestinationAirport().getAirportCode());
    }

    @Test
    void testFindByDepartureDateTime() {
        FlightEntity flight = createSampleFlightEntity("FN128");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findByDepartureDateTime(flight.getDepartureDateTime());
        assertFalse(flights.isEmpty());
        assertEquals(flight.getDepartureDateTime(), flights.get(0).getDepartureDateTime());
    }

    @Test
    void testFindByLandingDateTime() {
        FlightEntity flight = createSampleFlightEntity("FN129");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findByLandingDateTime(flight.getLandingDateTime());
        assertFalse(flights.isEmpty());
        assertEquals(flight.getLandingDateTime(), flights.get(0).getLandingDateTime());
    }

    @Test
    void testFindBySourceAndDestinationAirportCode() {
        FlightEntity flight = createSampleFlightEntity("FN130");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCode("JFK", "LAX");
        assertFalse(flights.isEmpty());
        assertEquals("JFK", flights.get(0).getSourceAirport().getAirportCode());
        assertEquals("LAX", flights.get(0).getDestinationAirport().getAirportCode());
    }

    @Test
    void testFindBySourceAirportAndDepartureDateTime() {
        FlightEntity flight = createSampleFlightEntity("FN131");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDepartureDateTime("JFK", flight.getDepartureDateTime());
        assertFalse(flights.isEmpty());
        assertEquals("JFK", flights.get(0).getSourceAirport().getAirportCode());
        assertEquals(flight.getDepartureDateTime(), flights.get(0).getDepartureDateTime());
    }

    @Test
    void testFindByDestinationAirportAndLandingDateTime() {
        FlightEntity flight = createSampleFlightEntity("FN132");
        flightRepository.save(flight);

        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCodeAndLandingDateTime("LAX", flight.getLandingDateTime());
        assertFalse(flights.isEmpty());
        assertEquals("LAX", flights.get(0).getDestinationAirport().getAirportCode());
        assertEquals(flight.getLandingDateTime(), flights.get(0).getLandingDateTime());
    }

    @Test
    void testDelete() {
        FlightEntity flight = createSampleFlightEntity("FN133");
        flightRepository.save(flight);

        flightRepository.deleteById("FN133");

        Optional<FlightEntity> found = flightRepository.findById("FN133");
        assertFalse(found.isPresent());
    }

    private FlightEntity createSampleFlightEntity(String flightNumber) {
        AirportEntity sourceAirport = new AirportEntity();
        sourceAirport.setAirportCode("JFK");
        sourceAirport.setAirportName("John F. Kennedy International Airport");
        airportRepository.save(sourceAirport); // save sourceAirport

        AirportEntity destinationAirport = new AirportEntity();
        destinationAirport.setAirportCode("LAX");
        destinationAirport.setAirportName("Los Angeles International Airport");
        airportRepository.save(destinationAirport); // save destinationAirport

        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Boeing737");
        vehicleTypeRepository.save(vehicleType); // save vehicleType

        PlaneEntity plane = new PlaneEntity();
        plane.setPlaneId(83661823);
        plane.setVehicleType(vehicleType);
        planeRepository.save(plane); // save plane

        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber(flightNumber);
        flight.setSourceAirport(sourceAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setPlane(plane);
        flight.setDepartureDateTime(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS));
        flight.setLandingDateTime(LocalDateTime.now().plusDays(1).plusHours(5).truncatedTo(ChronoUnit.MILLIS));
        flight.setFlightRange(3000);
        flight.setSharedFlight(false);

        return flight;
    }
}
