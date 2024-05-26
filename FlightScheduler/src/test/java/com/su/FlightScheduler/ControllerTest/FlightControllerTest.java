package com.su.FlightScheduler.ControllerTest;

import com.su.FlightScheduler.APIs.FlightAPI.FlightController;
import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.Seats;
import com.su.FlightScheduler.Entity.FlightEntitites.*;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private FlightDataDTO flightDataDTO;
    private FlightEntity flightEntity;
    private AirportEntity sourceAirport;
    private AirportEntity destinationAirport;
    private PlaneEntity plane;
    private CompanyEntity company;
    private AdminEntity admin;


    private VehicleTypeEntity vehicleType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CityEntity sourceCity = new CityEntity();
        sourceCity.setCityName("Istanbul");
        sourceCity.setLatitude(42.0);
        sourceCity.setLongitude(29.0);

        CityEntity destinationCity = new CityEntity();
        destinationCity.setCityName("Ankara");
        destinationCity.setLatitude(39.0);
        destinationCity.setLongitude(35.0);

        sourceAirport = new AirportEntity();
        sourceAirport.setAirportCode("IST");
        sourceAirport.setAirportName("Istanbul Airport");
        sourceAirport.setCity(sourceCity);

        destinationAirport = new AirportEntity();
        destinationAirport.setAirportCode("ESB");
        destinationAirport.setAirportName("Esenboga Airport");
        destinationAirport.setCity(destinationCity);

        vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Boeing3169");
        vehicleType.setSeniorAttendeeCapacity(2);
        vehicleType.setJuniorAttendeeCapacity(2);
        vehicleType.setSeatingPlan("2|2*10=2|2*40");

        plane = new PlaneEntity();
        plane.setPlaneId(3169);
        plane.setVehicleType(vehicleType);

        company = new CompanyEntity();
        company.setCompanyName("TestAirlines");

        admin = new AdminEntity();
        admin.setAdminId(316962);

        flightEntity = new FlightEntity();
        flightEntity.setFlightNumber("SU1234");
        flightEntity.setFlightInfo("Regular flight");
        flightEntity.setSourceAirport(sourceAirport);
        flightEntity.setDestinationAirport(destinationAirport);
        flightEntity.setPlane(plane);
        flightEntity.setFlightRange(500);
        flightEntity.setDepartureDateTime(LocalDateTime.now().plusHours(2));
        flightEntity.setLandingDateTime(LocalDateTime.now().plusHours(5));
        flightEntity.setSharedFlight(false);
        flightEntity.setSharedFlightCompany(null);
        flightEntity.setAdmin(admin);
        flightEntity.setStandardMenu("Standard Menu");

        flightDataDTO = new FlightDataDTO();
        flightDataDTO.setFrom("Istanbul");
        flightDataDTO.setGoTo("Ankara");
        flightDataDTO.setDepartureAirport("IST");
        flightDataDTO.setLandingAirport("ESB");
        flightDataDTO.setDepartureTime(LocalDateTime.now().plusHours(2));
        flightDataDTO.setLandingTime(LocalDateTime.now().plusHours(5));
        flightDataDTO.setPlaneType("Boeing3169");
        flightDataDTO.setAirlineCompany("TestAirlines");
        flightDataDTO.setFlightId("SU1234");  // Set the correct flightId here
        flightDataDTO.setPlaneId("3169");
        flightDataDTO.setMenu("Standard Menu");
    }


    @Test
    public void testSaveFlightFromDTO_Success() {
        when(flightService.createFlight(any(FlightDataDTO.class), anyInt())).thenReturn(flightEntity);

        ResponseEntity<Object> response = flightController.saveFlightFromDTO(flightDataDTO, 316962);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Object responseBody = response.getBody();
        System.out.println("Response Body: " + responseBody);
        assertNotNull(responseBody);

        if (responseBody instanceof FlightDataDTO) {
            FlightDataDTO savedFlightDTO = (FlightDataDTO) responseBody;
            System.out.println("Saved Flight DTO: " + savedFlightDTO);
            assertEquals("SU1234", savedFlightDTO.getFlightId());
        } else {
            fail("Response body is not of type FlightDataDTO");
        }
    }


    @Test
    public void testSaveFlightFromDTO_Fail() {
        when(flightService.createFlight(any(FlightDataDTO.class), anyInt())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Object> response = flightController.saveFlightFromDTO(flightDataDTO, 316962);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void testSaveFlight_Success() {
        when(flightService.saveFlightObj(any(FlightEntity.class))).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.saveFlight(flightEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity savedFlight = (FlightEntity) response.getBody();
        assertNotNull(savedFlight);
        assertEquals("SU1234", savedFlight.getFlightNumber());
    }

    @Test
    public void testSaveFlight_Fail() {
        when(flightService.saveFlightObj(any(FlightEntity.class))).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.saveFlight(flightEntity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    public void testFindAllFlights_Success() {
        when(flightService.findAllFlights()).thenReturn(List.of(flightEntity));

        ResponseEntity<?> response = flightController.findAllFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightEntity> flights = (List<FlightEntity>) response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightNumber());
    }

    @Test
    public void testFindAllFlights_Fail() {
        when(flightService.findAllFlights()).thenThrow(new RuntimeException("No flights found"));

        ResponseEntity<?> response = flightController.findAllFlights();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No flights found", response.getBody());
    }

    @Test
    public void testFindFlightByNumber_Success() {
        when(flightService.findFlightByNumberDTO("SU1234")).thenReturn(flightDataDTO);

        ResponseEntity<?> response = flightController.findFlightByNumber("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightDataDTO flightDTO = (FlightDataDTO) response.getBody();
        assertNotNull(flightDTO);
        assertEquals("SU1234", flightDTO.getFlightId());
    }

    @Test
    public void testFindFlightByNumber_Fail() {
        when(flightService.findFlightByNumberDTO("SU1234")).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.findFlightByNumber("SU1234");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    public void testGetAllFlights_Success() {
        when(flightService.findAllFlightsDTO()).thenReturn(List.of(flightDataDTO));

        ResponseEntity<Object> response = flightController.getAllFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = (List<FlightDataDTO>) response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAirport_Success() {
        when(flightService.findFlightsByDepartureAirport("IST")).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureAirport("IST");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDestinationAirport_Success() {
        when(flightService.findFlightsByDestinationAirport("ESB")).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDestinationAirport("ESB");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirport_Success() {
        when(flightService.findFlightsByDepartureAndDestinationAirport("IST", "ESB")).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureAndDestinationAirport("IST", "ESB");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureDateTime_Success() {
        when(flightService.findFlightsByDepartureDateTime(any(LocalDateTime.class))).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureDateTime(LocalDateTime.now().plusHours(2));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByLandingDateTime_Success() {
        when(flightService.findFlightsByLandingDateTime(any(LocalDateTime.class))).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByLandingDateTime(LocalDateTime.now().plusHours(5));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndLandingDateTime_Success() {
        when(flightService.findFlightsByDepartureAndLandingDateTime(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureAndLandingDateTime(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testUpdateFlightByFightDTO_Success() {
        when(flightService.updateFlightByFlightDTO(any(FlightDataDTO.class), anyInt())).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateFlightByFightDTO(flightDataDTO, 316962);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals("SU1234", updatedFlight.getFlightNumber());
    }

    @Test
    public void testDeleteFlightByNumber_Success() {
        doNothing().when(flightService).deleteFlightByNumber("SU1234");

        ResponseEntity<?> response = flightController.deleteFlightByNumber("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(flightService, times(1)).deleteFlightByNumber("SU1234");
    }

    @Test
    public void testUpdateFlightInfo_Success() {
        flightEntity.setFlightInfo("Updated flight info");
        when(flightService.updateFlightInfo("SU1234", "Updated flight info")).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateFlightInfo("SU1234", "Updated flight info");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals("Updated flight info", updatedFlight.getFlightInfo());
    }

    @Test
    public void testUpdateSourceAirport_Success() {
        when(flightService.updateSourceAirport("SU1234", sourceAirport)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateSourceAirport("SU1234", sourceAirport);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(sourceAirport, updatedFlight.getSourceAirport());
    }

    @Test
    public void testUpdateDestinationAirport_Success() {
        when(flightService.updateDestinationAirport("SU1234", destinationAirport)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateDestinationAirport("SU1234", destinationAirport);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(destinationAirport, updatedFlight.getDestinationAirport());
    }

    @Test
    public void testUpdatePlane_Success() {
        when(flightService.updatePlane("SU1234", plane)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updatePlane("SU1234", plane);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(plane, updatedFlight.getPlane());
    }

    @Test
    public void testUpdateDepartureDateTime_Success() {
        LocalDateTime newDepartureDateTime = LocalDateTime.now().plusDays(1);
        flightEntity.setDepartureDateTime(newDepartureDateTime);
        when(flightService.updateDepartureDateTime("SU1234", newDepartureDateTime)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateDepartureDateTime("SU1234", newDepartureDateTime.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(newDepartureDateTime, updatedFlight.getDepartureDateTime());
    }

    @Test
    public void testUpdateLandingDateTime_Success() {
        LocalDateTime newLandingDateTime = LocalDateTime.now().plusDays(1);
        flightEntity.setLandingDateTime(newLandingDateTime);
        when(flightService.getFlightOrThrow("SU1234")).thenReturn(flightEntity); // Add this line
        when(flightService.updateLandingDateTime("SU1234", newLandingDateTime)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateLandingDateTime("SU1234", newLandingDateTime.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(newLandingDateTime, updatedFlight.getLandingDateTime());
    }

    @Test
    public void testUpdateSharedFlightCompany_Success() {
        flightEntity.setSharedFlightCompany(company);
        when(flightService.updateSharedFlightCompany("SU1234", company)).thenReturn(flightEntity);

        ResponseEntity<?> response = flightController.updateSharedFlightCompany("SU1234", company);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightEntity updatedFlight = (FlightEntity) response.getBody();
        assertNotNull(updatedFlight);
        assertEquals(company, updatedFlight.getSharedFlightCompany());
    }

    @Test
    public void testGetSourceAirport_Success() {
        when(flightService.getSourceAirport("SU1234")).thenReturn(sourceAirport);

        ResponseEntity<?> response = flightController.getSourceAirport("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AirportEntity airport = (AirportEntity) response.getBody();
        assertNotNull(airport);
        assertEquals("IST", airport.getAirportCode());
    }

    @Test
    public void testGetDestinationAirport_Success() {
        when(flightService.getDestinationAirport("SU1234")).thenReturn(destinationAirport);

        ResponseEntity<?> response = flightController.getDestinationAirport("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AirportEntity airport = (AirportEntity) response.getBody();
        assertNotNull(airport);
        assertEquals("ESB", airport.getAirportCode());
    }

    @Test
    public void testGetPlane_Success() {
        when(flightService.getPlane("SU1234")).thenReturn(plane);

        ResponseEntity<?> response = flightController.getPlane("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlaneEntity planeEntity = (PlaneEntity) response.getBody();
        assertNotNull(planeEntity);
        assertEquals(3169, planeEntity.getPlaneId());
    }

    @Test
    public void testGetCompany_Success() {
        when(flightService.getCompany("SU1234")).thenReturn(company);

        ResponseEntity<?> response = flightController.getCompany("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CompanyEntity companyEntity = (CompanyEntity) response.getBody();
        assertNotNull(companyEntity);
        assertEquals("TestAirlines", companyEntity.getCompanyName());
    }

    @Test
    public void testGetDateTime_Success() {
        LocalDateTime departureTime = LocalDateTime.now().plusHours(2);
        when(flightService.getDateTime("SU1234")).thenReturn(departureTime);

        ResponseEntity<?> response = flightController.getDateTime("SU1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalDateTime dateTime = (LocalDateTime) response.getBody();
        assertNotNull(dateTime);
        assertEquals(departureTime, dateTime);
    }

    @Test
    public void testGetSeats_Success() {
        when(flightService.decodeSeatingPlan("SU1234")).thenReturn(List.of(new SeatingTypeDTO()));
        when(flightService.findBookedFlightsByFlightNumber("SU1234")).thenReturn(List.of(new SeatingDTO()));

        Seats seats = flightController.getSeats("SU1234");

        assertNotNull(seats);
        assertEquals(1, seats.getSeatList().size());
        assertEquals(1, seats.getSeatingList().size());
    }

    @Test
    public void testHandleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<String> response = flightController.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    public void testSaveFlightFromDTO_InvalidData() {
        flightDataDTO.setFlightId(null);
        when(flightService.createFlight(any(FlightDataDTO.class), anyInt())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Object> response = flightController.saveFlightFromDTO(flightDataDTO, 316962);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void testFindAllFlights_NoFlights() {
        when(flightService.findAllFlights()).thenReturn(List.of());

        ResponseEntity<?> response = flightController.findAllFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightEntity> flights = (List<FlightEntity>) response.getBody();
        assertTrue(flights.isEmpty());
    }

    @Test
    public void testFindFlightByNumber_NonExistentFlight() {
        when(flightService.findFlightByNumberDTO("SU1234")).thenThrow(new RuntimeException("Flight not found"));

        ResponseEntity<?> response = flightController.findFlightByNumber("SU1234");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    public void testFindFlightsByDepartureAirportAndDepartureDateTime_Success() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusHours(2);
        when(flightService.findFlightsByDepartureAirportAndDepartureDateTime("IST", departureDateTime)).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureAirportAndDepartureDateTime("IST", departureDateTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDestinationAirportAndLandingDateTime_Success() {
        LocalDateTime landingDateTime = LocalDateTime.now().plusHours(5);
        when(flightService.findFlightsByDestinationAirportAndLandingDateTime("ESB", landingDateTime)).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDestinationAirportAndLandingDateTime("ESB", landingDateTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime_Success() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusHours(2);
        LocalDateTime landingDateTime = LocalDateTime.now().plusHours(5);
        when(flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime("IST", "ESB", departureDateTime, landingDateTime)).thenReturn(List.of(flightDataDTO));

        ResponseEntity<List<FlightDataDTO>> response = flightController.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime("IST", "ESB", departureDateTime, landingDateTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDataDTO> flights = response.getBody();
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testUpdateFlightInfo_NullFlightInfo() {
        Exception exception = assertThrows(FlightController.BadRequestException.class, () -> {
            flightController.updateFlightInfo("SU1234", null);
        });

        assertEquals("Flight info cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateSourceAirport_NullSourceAirport() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightController.updateSourceAirport("SU1234", null);
        });

        assertEquals("Source airport cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdateDestinationAirport_NullDestinationAirport() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightController.updateDestinationAirport("SU1234", null);
        });

        assertEquals("Destination airport cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdatePlane_NullPlane() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightController.updatePlane("SU1234", null);
        });

        assertEquals("Plane cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdateDepartureDateTime_PastDateTime() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusHours(1);
        Exception exception = assertThrows(FlightController.BadRequestException.class, () -> {
            flightController.updateDepartureDateTime("SU1234", pastDateTime.toString());
        });

        assertEquals("Departure time cannot be in the past", exception.getMessage());
    }

    @Test
    public void testUpdateLandingDateTime_BeforeDepartureTime() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusHours(2);
        LocalDateTime landingDateTime = LocalDateTime.now().plusHours(1);
        when(flightService.getFlightOrThrow("SU1234")).thenReturn(flightEntity);
        flightEntity.setDepartureDateTime(departureDateTime);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightController.updateLandingDateTime("SU1234", landingDateTime.toString());
        });

        assertEquals("Landing time cannot be before departure time", exception.getMessage());
    }

    @Test
    public void testUpdateSharedFlightCompany_NullCompany() {
        Exception exception = assertThrows(FlightController.BadRequestException.class, () -> {
            flightController.updateSharedFlightCompany("SU1234", null);
        });

        assertEquals("Shared flight company cannot be null", exception.getMessage());
    }
}
