package com.su.FlightScheduler.ControllerTest;

import com.su.FlightScheduler.APIs.FlightAPI.FlightController;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.Seats;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private FlightEntity flight;
    private AdminEntity admin;
    private AirportEntity airport;
    private PlaneEntity plane;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new AdminEntity();
        admin.setAdminId(1);

        airport = new AirportEntity();
        airport.setAirportCode("JFK");

        plane = new PlaneEntity();
        plane.setPlaneId(1);

        company = new CompanyEntity();
        company.setCompanyName("AirlineCompany");

        flight = new FlightEntity();
        flight.setFlightNumber("FL123");
        flight.setAdmin(admin);
        flight.setSourceAirport(airport);
        flight.setDestinationAirport(airport);
        flight.setPlane(plane);
        flight.setFlightRange(1000);
        flight.setDepartureDateTime(LocalDateTime.now());
        flight.setLandingDateTime(LocalDateTime.now().plusHours(2));
        flight.setSharedFlight(false);
        flight.setSharedFlightCompany(company);
    }

    @Test
    void testSaveFlight() {
        when(flightService.saveFlightObj(any(FlightEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.saveFlight(flight);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testSaveFlight_FlightNotFound() {
        when(flightService.saveFlightObj(any(FlightEntity.class))).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<?> response = flightController.saveFlight(flight);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testCreateFlightFilled() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightInfo", "Info");
        request.put("admin", admin);

        when(flightService.createFlight(anyString(), anyString(), any(AdminEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.createFlightFilled(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testCreateFlightFilled_FlightNotFound() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightInfo", "Info");
        request.put("admin", admin);

        when(flightService.createFlight(anyString(), anyString(), any(AdminEntity.class))).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<?> response = flightController.createFlightFilled(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testCreateFlight() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightInfo", "Info");
        request.put("admin", admin);

        when(flightService.createFlight(anyString(), anyString(), any(AdminEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.createFlight(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testCreateFlight_FlightNotFound() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightInfo", "Info");
        request.put("admin", admin);

        when(flightService.createFlight(anyString(), anyString(), any(AdminEntity.class))).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<?> response = flightController.createFlight(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testAddFlightParams1() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("plane", plane);
        request.put("sourceAirport", airport);
        request.put("destinationAirport", airport);

        when(flightService.getPlaneFromRequest(any(Map.class), anyString())).thenReturn(plane);
        when(flightService.getAirportFromRequest(any(Map.class), anyString())).thenReturn(airport);
        when(flightService.addFlightParams1(anyString(), any(PlaneEntity.class), any(AirportEntity.class), any(AirportEntity.class))).thenReturn(flight);

        ResponseEntity<?> response = flightController.addFlightParams1(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testAddFlightParams1_FlightNotFound() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("plane", plane);
        request.put("sourceAirport", airport);
        request.put("destinationAirport", airport);

        when(flightService.getPlaneFromRequest(any(Map.class), anyString())).thenReturn(plane);
        when(flightService.getAirportFromRequest(any(Map.class), anyString())).thenReturn(airport);
        when(flightService.addFlightParams1(anyString(), any(PlaneEntity.class), any(AirportEntity.class), any(AirportEntity.class))).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.addFlightParams1(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testAddFlightParams2() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightRange", 1000);
        request.put("departureDateTime", "2024-01-01T10:00:00");
        request.put("landingDateTime", "2024-01-01T12:00:00");

        when(flightService.getDateTimeFromRequest(any(Map.class), anyString())).thenReturn(LocalDateTime.parse("2024-01-01T10:00:00"));
        when(flightService.addFlightParams2(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(flight);

        ResponseEntity<?> response = flightController.addFlightParams2(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testAddFlightParams2_FlightNotFound() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("flightRange", 1000);
        request.put("departureDateTime", "2024-01-01T10:00:00");
        request.put("landingDateTime", "2024-01-01T12:00:00");

        when(flightService.getDateTimeFromRequest(any(Map.class), anyString())).thenReturn(LocalDateTime.parse("2024-01-01T10:00:00"));
        when(flightService.addFlightParams2(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.addFlightParams2(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testAddFlightParams3() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("sharedFlight", true);
        request.put("sharedFlightCompany", company);

        when(flightService.getCompanyFromRequest(any(Map.class), anyString())).thenReturn(company);
        when(flightService.addFlightParams3(anyString(), anyBoolean(), any(CompanyEntity.class))).thenReturn(flight);

        ResponseEntity<?> response = flightController.addFlightParams3(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testAddFlightParams3_FlightNotFound() {
        Map<String, Object> request = new HashMap<>();
        request.put("flightNumber", "FL123");
        request.put("sharedFlight", true);
        request.put("sharedFlightCompany", company);

        when(flightService.getCompanyFromRequest(any(Map.class), anyString())).thenReturn(company);
        when(flightService.addFlightParams3(anyString(), anyBoolean(), any(CompanyEntity.class))).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.addFlightParams3(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testFindFlightByNumber() {
        when(flightService.findFlightByNumber(anyString())).thenReturn(Optional.of(flight));
        ResponseEntity<?> response = flightController.findFlightByNumber("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testFindFlightByNumber_FlightNotFound() {
        when(flightService.findFlightByNumber(anyString())).thenReturn(Optional.empty());
        ResponseEntity<?> response = flightController.findFlightByNumber("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testFindAllFlights() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findAllFlights()).thenReturn(flights);
        ResponseEntity<?> response = flightController.findAllFlights();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindAllFlights_NoFlightsFound() {
        when(flightService.findAllFlights()).thenThrow(new RuntimeException("No flights found"));
        ResponseEntity<?> response = flightController.findAllFlights();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No flights found", response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAirport() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAirport(anyString())).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAirport("JFK");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDestinationAirport() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDestinationAirport(anyString())).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDestinationAirport("LAX");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAndDestinationAirport() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAndDestinationAirport(anyString(), anyString())).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAndDestinationAirport("JFK", "LAX");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureDateTime(any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureDateTime(LocalDateTime.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByLandingDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByLandingDateTime(any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByLandingDateTime(LocalDateTime.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAndLandingDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAndLandingDateTime(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAndLandingDateTime(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAirportAndDepartureDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAirportAndDepartureDateTime(anyString(), any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAirportAndDepartureDateTime("JFK", LocalDateTime.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDestinationAirportAndLandingDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDestinationAirportAndLandingDateTime(anyString(), any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDestinationAirportAndLandingDateTime("LAX", LocalDateTime.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime("JFK", "LAX", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testFindFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime() {
        List<FlightEntity> flights = Collections.singletonList(flight);
        when(flightService.findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(flights);
        ResponseEntity<List<FlightEntity>> response = flightController.findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime("JFK", "LAX", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testUpdateFlight() {
        when(flightService.updateFlight(any(FlightEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateFlight(flight);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testDeleteFlightByNumber() {
        doNothing().when(flightService).deleteFlightByNumber(anyString());
        ResponseEntity<?> response = flightController.deleteFlightByNumber("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateFlightInfo() {
        when(flightService.updateFlightInfo(anyString(), anyString())).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateFlightInfo("FL123", "Updated Info");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdateSourceAirport() {
        when(flightService.updateSourceAirport(anyString(), any(AirportEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateSourceAirport("FL123", airport);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdateDestinationAirport() {
        when(flightService.updateDestinationAirport(anyString(), any(AirportEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateDestinationAirport("FL123", airport);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdatePlane() {
        when(flightService.updatePlane(anyString(), any(PlaneEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updatePlane("FL123", plane);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdateDepartureDateTime() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        when(flightService.updateDepartureDateTime(anyString(), any(LocalDateTime.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateDepartureDateTime("FL123", newDateTime.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdateLandingDateTime() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1).plusHours(2);
        when(flightService.updateLandingDateTime(anyString(), any(LocalDateTime.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateLandingDateTime("FL123", newDateTime.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testUpdateSharedFlightCompany() {
        when(flightService.updateSharedFlightCompany(anyString(), any(CompanyEntity.class))).thenReturn(flight);
        ResponseEntity<?> response = flightController.updateSharedFlightCompany("FL123", company);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testGetSourceAirport() {
        when(flightService.getSourceAirport(anyString())).thenReturn(airport);
        ResponseEntity<?> response = flightController.getSourceAirport("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(airport, response.getBody());
    }

    @Test
    void testGetDestinationAirport() {
        when(flightService.getDestinationAirport(anyString())).thenReturn(airport);
        ResponseEntity<?> response = flightController.getDestinationAirport("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(airport, response.getBody());
    }

    @Test
    void testGetPlane() {
        when(flightService.getPlane(anyString())).thenReturn(plane);
        ResponseEntity<?> response = flightController.getPlane("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plane, response.getBody());
    }

    @Test
    void testGetCompany() {
        when(flightService.getCompany(anyString())).thenReturn(company);
        ResponseEntity<?> response = flightController.getCompany("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(company, response.getBody());
    }

    @Test
    void testGetDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        when(flightService.getDateTime(anyString())).thenReturn(dateTime);
        ResponseEntity<?> response = flightController.getDateTime("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dateTime, response.getBody());
    }

    @Test
    void testGetSeats() {
        List<SeatingTypeDTO> seatingTypeList = new ArrayList<>();
        List<SeatingDTO> seatingDTOList = new ArrayList<>();

        when(flightService.decodeSeatingPlan(anyString())).thenReturn(seatingTypeList);
        when(flightService.findBookedFlightsByFlightNumber(anyString())).thenReturn(seatingDTOList);

        Seats seats = flightController.getSeats("FL123");
        assertNotNull(seats);
        assertEquals(seatingTypeList, seats.getSeatList());
        assertEquals(seatingDTOList, seats.getSeatingList());
    }
}
