package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Entity.FlightEntitites.*;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Service.PilotFlightAssignmentServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PilotFlightAssignmentServiceTests {

    @InjectMocks
    private PilotFlightAssignmentServiceImp pilotFlightAssignmentService;

    @Mock
    private PilotRepository pilotRepository;

    @Mock
    private PilotAssignmentRepository pilotAssignmentRepository;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TESTS FOR getFlightsOfPilot()
    /*
    @Test
    public void getFlightsOfPilot_PilotExists() {

        List<PilotAssignmentEntity> assignments = new ArrayList<>();

        List<PilotLanguageEntity> languages = new ArrayList<>();
        languages.add(new PilotLanguageEntity(new PilotLanguagePK(1, "English")));
        languages.add(new PilotLanguageEntity(new PilotLanguagePK(1, "Spanish")));

        PilotEntity pilotEntity = new PilotEntity(

                "dummy@example.com",
                "password123",
                "John",
                "Doe",
                35,
                "Male",
                10000,
                "American",
                "Senior Captain",
                languages,
                assignments
        );

        int pilotId = 1;
        pilotEntity.setPilotId(pilotId);

        when(pilotRepository.findById(pilotId)).thenReturn(Optional.of(pilotEntity));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId)).thenReturn(assignments);

        UserDataDTO result = pilotFlightAssignmentService.getFlightsOfPilot(pilotId);

        assertNotNull(result);
        verify(pilotRepository, times(1)).findById(pilotId);
        verify(pilotAssignmentRepository, times(1)).findAllByPilotAssignmentPK_PilotId(pilotId);
    }

     */

    @Test
    public void getFlightsOfPilot_PilotDoesNotExist() {
        int pilotId = 1;

        when(pilotRepository.findById(pilotId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.getFlightsOfPilot(pilotId);
        });

        assertEquals("Pilot with id: " + pilotId + " does not exist!", exception.getMessage());
        verify(pilotRepository, times(1)).findById(pilotId);
    }

    // TESTS FOR getAvailablePilotsForFlight()
    /*
    @Test
    public void getAvailablePilotsForFlight_FlightExists() {
        CityEntity dummyCity = new CityEntity();
        dummyCity.setCityName("DummyCity");

        AirportEntity source = new AirportEntity(
                "DUM123",  // dummy airport code
                dummyCity, // dummy city entity
                "DummyCountry",
                "Dummy Airport"
        );

        CityEntity anotherDummyCity = new CityEntity();
        anotherDummyCity.setCityName("AnotherDummyCity");

        AirportEntity destination = new AirportEntity(
                "ADUM456",  // another dummy airport code
                anotherDummyCity, // another dummy city entity
                "AnotherDummyCountry",
                "Another Dummy Airport"
        );

        VehicleTypeEntity dummyVehicleType = new VehicleTypeEntity();
        dummyVehicleType.setVehicleType("DummyType");
        dummyVehicleType.setBusinessCapacity(20);
        dummyVehicleType.setEconomyCapacity(200);
        dummyVehicleType.setSeniorPilotCapacity(2);
        dummyVehicleType.setJuniorPilotCapacity(4);
        dummyVehicleType.setTraineePilotCapacity(2);
        dummyVehicleType.setSeniorAttendeeCapacity(5);
        dummyVehicleType.setJuniorAttendeeCapacity(10);
        dummyVehicleType.setChefAttendeeCapacity(2);
        dummyVehicleType.setSeatingPlan("A seating plan example");

        // Creating a dummy PlaneEntity object
        PlaneEntity dummyPlane = new PlaneEntity();
        dummyPlane.setPlaneId(1);
        dummyPlane.setVehicleType(dummyVehicleType);


        String flightNumber = "FL123";
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(LocalDateTime.now());
        flight.setFlightRange(1000);
        flight.setSourceAirport(source);
        flight.setDestinationAirport(destination);
        flight.setPlane(dummyPlane);

        List<PilotAssignmentEntity> assignments = new ArrayList<>();
        List<PilotEntity> availablePilots = new ArrayList<>();

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(flight));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber)).thenReturn(assignments);
        when(pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual(anyString(), anyInt())).thenReturn(availablePilots);

        List<UserDataDTO> result = pilotFlightAssignmentService.getAvailablePilotsForFlight(flightNumber);

        assertNotNull(result);
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(flightRepository, times(1)).findById(flightNumber);
        verify(pilotAssignmentRepository, times(1)).findAllByPilotAssignmentPK_FlightNumber(flightNumber);
        verify(pilotRepository, times(1)).findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual(anyString(), anyInt());
    }

     */

    @Test
    public void getAvailablePilotsForFlight_FlightDoesNotExist() {
        String flightNumber = "FL123";

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.getAvailablePilotsForFlight(flightNumber);
        });

        assertEquals("Flight with id: " + flightNumber + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }

    // TESTS FOR assignPilotToFlight()
    /*
    @Test
    public void assignPilotToFlight_Success() {

        VehicleTypeEntity dummyVehicleType = new VehicleTypeEntity();
        dummyVehicleType.setVehicleType("DummyType");
        dummyVehicleType.setBusinessCapacity(20);
        dummyVehicleType.setEconomyCapacity(200);
        dummyVehicleType.setSeniorPilotCapacity(2);
        dummyVehicleType.setJuniorPilotCapacity(4);
        dummyVehicleType.setTraineePilotCapacity(2);
        dummyVehicleType.setSeniorAttendeeCapacity(5);
        dummyVehicleType.setJuniorAttendeeCapacity(10);
        dummyVehicleType.setChefAttendeeCapacity(2);
        dummyVehicleType.setSeatingPlan("A seating plan example");

        // Creating a dummy PlaneEntity object
        PlaneEntity dummyPlane = new PlaneEntity();
        dummyPlane.setPlaneId(1);
        dummyPlane.setVehicleType(dummyVehicleType);

        String flightNumber = "FL123";
        int pilotId = 1;
        FlightEntity flight = new FlightEntity();
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);
        pilot.setEmail("example@example.com");
        List<PilotAssignmentEntity> assignments = new ArrayList<>();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(LocalDateTime.now());
        flight.setFlightRange(1000);
        flight.setPlane(dummyPlane);



        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(pilotRepository.existsById(pilotId)).thenReturn(true);
        when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(flight));
        when(pilotRepository.findById(pilotId)).thenReturn(Optional.of(pilot));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber)).thenReturn(assignments);
        when(pilotAssignmentRepository.save(any(PilotAssignmentEntity.class))).thenReturn(new PilotAssignmentEntity());

        UserDataDTO result = pilotFlightAssignmentService.assignPilotToFlight(flightNumber, pilotId);

        assertNotNull(result);
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(pilotRepository, times(1)).existsById(pilotId);
        verify(flightRepository, times(1)).findById(flightNumber);
        verify(pilotRepository, times(1)).findById(pilotId);
        verify(pilotAssignmentRepository, times(1)).findAllByPilotAssignmentPK_FlightNumber(flightNumber);
        verify(pilotAssignmentRepository, times(1)).save(any(PilotAssignmentEntity.class));
    }

     */

    @Test
    public void assignPilotToFlight_FlightDoesNotExist() {
        String flightNumber = "FL123";
        int pilotId = 1;

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.assignPilotToFlight(flightNumber, pilotId);
        });

        assertEquals("Flight with id: " + flightNumber + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }

    @Test
    public void assignPilotToFlight_PilotDoesNotExist() {
        String flightNumber = "FL123";
        int pilotId = 1;

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(pilotRepository.existsById(pilotId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.assignPilotToFlight(flightNumber, pilotId);
        });

        assertEquals("Pilot with id: " + pilotId + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(pilotRepository, times(1)).existsById(pilotId);
    }

    // TESTS FOR getPilotsOfFlight()
    @Test
    public void getPilotsOfFlight_FlightExists() {
        String flightNumber = "FL123";
        List<PilotAssignmentEntity> assignments = new ArrayList<>();

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber)).thenReturn(assignments);

        List<UserDataDTO> result = pilotFlightAssignmentService.getPilotsOfFlight(flightNumber);

        assertNotNull(result);
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(pilotAssignmentRepository, times(1)).findAllByPilotAssignmentPK_FlightNumber(flightNumber);
    }

    @Test
    public void getPilotsOfFlight_FlightDoesNotExist() {
        String flightNumber = "FL123";

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.getPilotsOfFlight(flightNumber);
        });

        assertEquals("Flight with id: " + flightNumber + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }

    // TESTS FOR removeFlightFromAPilot()
    /*
    @Test
    public void removeFlightFromAPilot_AssignmentExists() {

        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);

        String flightNumber = "FL123";
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber(flightNumber);

        PilotAssignmentEntity assignment = new PilotAssignmentEntity();
        assignment.setPilot(pilot);
        assignment.setFlight(flight);

        when(pilotAssignmentRepository.findById(new PilotAssignmentPK(pilotId, flightNumber))).thenReturn(Optional.of(assignment));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId)).thenReturn(new ArrayList<>());
        doNothing().when(pilotAssignmentRepository).deleteById(new PilotAssignmentPK(pilotId, flightNumber));

        UserDataDTO result = pilotFlightAssignmentService.removeFlightFromAPilot(flightNumber, pilotId);

        assertNotNull(result);
        verify(pilotAssignmentRepository, times(1)).findById(new PilotAssignmentPK(pilotId, flightNumber));
        verify(pilotAssignmentRepository, times(1)).deleteById(new PilotAssignmentPK(pilotId, flightNumber));
    }

     */

    @Test
    public void removeFlightFromAPilot_AssignmentDoesNotExist() {
        String flightNumber = "FL123";
        int pilotId = 1;

        when(pilotAssignmentRepository.findById(new PilotAssignmentPK(pilotId, flightNumber))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotFlightAssignmentService.removeFlightFromAPilot(flightNumber, pilotId);
        });

        assertEquals("Cannot remove pilot with id: " + pilotId + " from flight with id: " + flightNumber + "!", exception.getMessage());
        verify(pilotAssignmentRepository, times(1)).findById(new PilotAssignmentPK(pilotId, flightNumber));
    }
}

