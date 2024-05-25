package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Service.PilotFlightAssignmentServiceImp;
import com.su.FlightScheduler.Util.FlightDateChecker;
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
    @Test
    public void getFlightsOfPilot_PilotExists() {
        int pilotId = 1;
        PilotEntity pilotEntity = new PilotEntity();
        pilotEntity.setPilotId(pilotId);
        List<PilotAssignmentEntity> assignments = new ArrayList<>();

        when(pilotRepository.findById(pilotId)).thenReturn(Optional.of(pilotEntity));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId)).thenReturn(assignments);

        UserDataDTO result = pilotFlightAssignmentService.getFlightsOfPilot(pilotId);

        assertNotNull(result);
        verify(pilotRepository, times(1)).findById(pilotId);
        verify(pilotAssignmentRepository, times(1)).findAllByPilotAssignmentPK_PilotId(pilotId);
    }

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
    @Test
    public void getAvailablePilotsForFlight_FlightExists() {
        String flightNumber = "FL123";
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(LocalDateTime.now());
        flight.setFlightRange(1000);
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
    @Test
    public void assignPilotToFlight_Success() {
        String flightNumber = "FL123";
        int pilotId = 1;
        FlightEntity flight = new FlightEntity();
        PilotEntity pilot = new PilotEntity();
        List<PilotAssignmentEntity> assignments = new ArrayList<>();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(LocalDateTime.now());
        flight.setFlightRange(1000);
        flight.setPlane(new PlaneEntity());

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
    @Test
    public void removeFlightFromAPilot_AssignmentExists() {
        String flightNumber = "FL123";
        int pilotId = 1;
        PilotAssignmentEntity assignment = new PilotAssignmentEntity();
        PilotEntity pilot = new PilotEntity();

        when(pilotAssignmentRepository.findById(new PilotAssignmentPK(pilotId, flightNumber))).thenReturn(Optional.of(assignment));
        when(pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId)).thenReturn(new ArrayList<>());
        doNothing().when(pilotAssignmentRepository).deleteById(new PilotAssignmentPK(pilotId, flightNumber));

        UserDataDTO result = pilotFlightAssignmentService.removeFlightFromAPilot(flightNumber, pilotId);

        assertNotNull(result);
        verify(pilotAssignmentRepository, times(1)).findById(new PilotAssignmentPK(pilotId, flightNumber));
        verify(pilotAssignmentRepository, times(1)).deleteById(new PilotAssignmentPK(pilotId, flightNumber));
    }

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

