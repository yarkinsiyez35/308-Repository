package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Entity.CompanyEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.*;
import com.su.FlightScheduler.Repository.*;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.PassengerFlightRepository;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;


import com.su.FlightScheduler.Service.FlightServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightServiceTests {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private PlaneRepository planeRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private VehicleTypeRepository vehicleTypeRepository;
    @Mock
    private PassengerFlightRepository passengerFlightRepository;
    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private FlightServiceImp flightService;

    private AirportEntity sourceAirport;
    private AirportEntity destinationAirport;
    private PlaneEntity plane;
    private CompanyEntity company;
    private AdminEntity admin;
    private FlightDataDTO flightDataDTO;
    private VehicleTypeEntity vehicleType;
    private FlightEntity flightEntity;

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
    public void testCreateFlight_Success() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity createdFlight = flightService.createFlight(flightDataDTO, 316962);

        assertNotNull(createdFlight);
        assertEquals("SU1234", createdFlight.getFlightNumber());
        assertEquals("Regular flight", createdFlight.getFlightInfo());
        assertEquals(sourceAirport, createdFlight.getSourceAirport());
        assertEquals(destinationAirport, createdFlight.getDestinationAirport());
        assertEquals(plane, createdFlight.getPlane());
        assertEquals(admin, createdFlight.getAdmin());
        assertEquals("Standard Menu", createdFlight.getStandardMenu());
    }

    @Test
    public void testCreateFlight_Fail_AirportNotFound() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, airports are wrong!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateFlight_Fail_PlaneNotFound() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, plane id does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateFlight_Fail_AdminNotFound() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(adminRepository.findById(316962)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, admin does not exist!";
        String actualMessage = exception.getMessage();

        System.out.println("Expected Message: " + expectedMessage);
        System.out.println("Actual Message: " + actualMessage);

        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void testCreateFlight_Fail_CompanyNotFound() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, the company does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateFlight_Fail_DistanceExceedsAllowed() {
        CityEntity distantCity = new CityEntity();
        distantCity.setCityName("DistantCity");
        distantCity.setLatitude(50.0);
        distantCity.setLongitude(10.0);

        AirportEntity distantAirport = new AirportEntity();
        distantAirport.setAirportCode("DST");
        distantAirport.setAirportName("Distant Airport");
        distantAirport.setCity(distantCity);

        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("DST")).thenReturn(Optional.of(distantAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));

        flightDataDTO.setLandingAirport("DST");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, the distance between the cities exceeds the allowed distance!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetFlightOrThrow_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        FlightEntity foundFlight = flightService.getFlightOrThrow("SU1234");

        assertNotNull(foundFlight);
        assertEquals("SU1234", foundFlight.getFlightNumber());
    }

    @Test
    public void testGetFlightOrThrow_Fail() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getFlightOrThrow("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindAllFlights_Success() {
        when(flightRepository.findAll()).thenReturn(List.of(flightEntity));

        List<FlightEntity> flights = flightService.findAllFlights();

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightNumber());
    }

    @Test
    public void testFindFlightsByDepartureAirport_Success() {
        when(flightRepository.findBySourceAirportAirportCode("IST")).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByDepartureAirport("IST");

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAirport_Fail() {
        when(flightRepository.findBySourceAirportAirportCode("IST")).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureAirport("IST");
        });

        String expectedMessage = "No flights found for the given departure airport";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDestinationAirport_Success() {
        when(flightRepository.findByDestinationAirportAirportCode("ESB")).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByDestinationAirport("ESB");

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDestinationAirport_Fail() {
        when(flightRepository.findByDestinationAirportAirportCode("ESB")).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDestinationAirport("ESB");
        });

        String expectedMessage = "No flights found for the given destination airport";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirport_Success() {
        when(flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCode("IST", "ESB")).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByDepartureAndDestinationAirport("IST", "ESB");

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirport_Fail() {
        when(flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCode("IST", "ESB")).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureAndDestinationAirport("IST", "ESB");
        });

        String expectedMessage = "No flights found for the given departure and destination airports";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDepartureDateTime_Success() {
        when(flightRepository.findByDepartureDateTime(any(LocalDateTime.class))).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByDepartureDateTime(LocalDateTime.now().plusHours(2));

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureDateTime_Fail() {
        when(flightRepository.findByDepartureDateTime(any(LocalDateTime.class))).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureDateTime(LocalDateTime.now().plusHours(2));
        });

        String expectedMessage = "No flights found for the given departure date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByLandingDateTime_Success() {
        when(flightRepository.findByLandingDateTime(any(LocalDateTime.class))).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByLandingDateTime(LocalDateTime.now().plusHours(5));

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByLandingDateTime_Fail() {
        when(flightRepository.findByLandingDateTime(any(LocalDateTime.class))).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByLandingDateTime(LocalDateTime.now().plusHours(5));
        });

        String expectedMessage = "No flights found for the given landing date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDepartureAndLandingDateTime_Success() {
        when(flightRepository.findByDepartureDateTimeAndLandingDateTime(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flights = flightService.findFlightsByDepartureAndLandingDateTime(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5));

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("SU1234", flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndLandingDateTime_Fail() {
        when(flightRepository.findByDepartureDateTimeAndLandingDateTime(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureAndLandingDateTime(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5));
        });

        String expectedMessage = "No flights found for the given departure and landing date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlightInfo_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateFlightInfo("SU1234", "Updated flight info");

        assertNotNull(updatedFlight);
        assertEquals("Updated flight info", updatedFlight.getFlightInfo());
    }

    @Test
    public void testUpdateFlightInfo_Fail() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateFlightInfo("SU1234", "Updated flight info");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateSourceAirport_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findById("IST")).thenReturn(Optional.of(sourceAirport));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateSourceAirport("SU1234", sourceAirport);

        assertNotNull(updatedFlight);
        assertEquals(sourceAirport, updatedFlight.getSourceAirport());
    }

    @Test
    public void testUpdateSourceAirport_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateSourceAirport("SU1234", sourceAirport);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateDestinationAirport_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findById("ESB")).thenReturn(Optional.of(destinationAirport));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateDestinationAirport("SU1234", destinationAirport);

        assertNotNull(updatedFlight);
        assertEquals(destinationAirport, updatedFlight.getDestinationAirport());
    }

    @Test
    public void testUpdateDestinationAirport_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateDestinationAirport("SU1234", destinationAirport);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdatePlane_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updatePlane("SU1234", plane);

        assertNotNull(updatedFlight);
        assertEquals(plane, updatedFlight.getPlane());
    }

    @Test
    public void testUpdatePlane_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updatePlane("SU1234", plane);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlightRange_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateFlightRange("SU1234", 600);

        assertNotNull(updatedFlight);
        assertEquals(600, updatedFlight.getFlightRange());
    }

    @Test
    public void testUpdateFlightRange_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateFlightRange("SU1234", 600);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateDepartureDateTime_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        LocalDateTime newDepartureTime = LocalDateTime.now().plusDays(1).plusHours(1);
        FlightEntity updatedFlight = flightService.updateDepartureDateTime("SU1234", newDepartureTime);

        assertNotNull(updatedFlight);
        assertEquals(newDepartureTime, updatedFlight.getDepartureDateTime());
    }

    @Test
    public void testUpdateDepartureDateTime_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateDepartureDateTime("SU1234", LocalDateTime.now().plusDays(1).plusHours(1));
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateLandingDateTime_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        LocalDateTime newLandingTime = LocalDateTime.now().plusDays(1).plusHours(3);
        FlightEntity updatedFlight = flightService.updateLandingDateTime("SU1234", newLandingTime);

        assertNotNull(updatedFlight);
        assertEquals(newLandingTime, updatedFlight.getLandingDateTime());
    }

    @Test
    public void testUpdateLandingDateTime_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateLandingDateTime("SU1234", LocalDateTime.now().plusDays(1).plusHours(3));
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateSharedFlight_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateSharedFlight("SU1234", true);

        assertNotNull(updatedFlight);
        assertTrue(updatedFlight.isSharedFlight());
    }

    @Test
    public void testUpdateSharedFlight_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateSharedFlight("SU1234", true);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateSharedFlightCompany_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateSharedFlightCompany("SU1234", company);

        assertNotNull(updatedFlight);
        assertEquals(company, updatedFlight.getSharedFlightCompany());
    }

    @Test
    public void testUpdateSharedFlightCompany_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateSharedFlightCompany("SU1234", company);
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateStandardMenu_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateStandardMenu("SU1234", "Updated Menu");

        assertNotNull(updatedFlight);
        assertEquals("Updated Menu", updatedFlight.getStandardMenu());
    }

    @Test
    public void testUpdateStandardMenu_Fail_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.updateStandardMenu("SU1234", "Updated Menu");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeleteFlightByNumber_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        doNothing().when(flightRepository).deleteById("SU1234");

        flightService.deleteFlightByNumber("SU1234");

        verify(flightRepository, times(1)).deleteById("SU1234");
    }

    @Test
    public void testDeleteFlightByNumber_Fail() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.deleteFlightByNumber("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindSeatingPlanByFlightNumber_Success() {
        when(flightRepository.findVehicleTypeByFlightId("SU1234")).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType("Boeing3169", VehicleTypeRepository.SeatingPlanProjection.class)).thenReturn(() -> "2|2*10=2|2*40");

        VehicleTypeRepository.SeatingPlanProjection seatingPlan = flightService.findSeatingPlanByFlightNumber("SU1234");

        assertNotNull(seatingPlan);
        assertEquals("2|2*10=2|2*40", seatingPlan.getSeatingPlan());
    }

    @Test
    public void testFindSeatingPlanByFlightNumber_Fail() {
        when(flightRepository.findVehicleTypeByFlightId("SU1234")).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findSeatingPlanByFlightNumber("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDecodeSeatingPlan_Success() {
        when(flightRepository.findVehicleTypeByFlightId("SU1234")).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType("Boeing3169", VehicleTypeRepository.SeatingPlanProjection.class)).thenReturn(() -> "2|2*10=2|2*40");

        List<SeatingTypeDTO> seatingList = flightService.decodeSeatingPlan("SU1234");

        assertNotNull(seatingList);
        assertEquals(2, seatingList.size());
        assertEquals("business", seatingList.get(0).getType());
        assertEquals("economy", seatingList.get(1).getType());
    }

    @Test
    public void testDecodeSeatingPlan_Fail() {
        when(flightRepository.findVehicleTypeByFlightId("SU1234")).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.decodeSeatingPlan("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindBookedFlightsByFlightNumber_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setSeatNumber("A1");
        PassengerEntity passenger = new PassengerEntity("com.com", "123", "John", "Doe", 25, "M", "USA");
        passengerFlight.setPassenger(passenger);
        when(passengerFlightRepository.findPassengerFlightByFlight(flightEntity)).thenReturn(List.of(passengerFlight));
        when(flightRepository.findVehicleTypeByFlightId("SU1234")).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType("Boeing3169", VehicleTypeRepository.SeatingPlanProjection.class)).thenReturn(() -> "2|2*10=2|2*40");

        List<SeatingDTO> seats = flightService.findBookedFlightsByFlightNumber("SU1234");

        assertNotNull(seats);
        assertEquals(201, seats.size()); // Expected 1 booked and 40 unbooked seats (based on the seating plan)
        long bookedSeatsCount = seats.stream().filter(SeatingDTO::isStatus).count();
        assertEquals(1, bookedSeatsCount); // Ensure there is exactly 1 booked seat
        assertTrue(seats.stream().anyMatch(seat -> "A1".equals(seat.getSeatPosition()) && seat.isStatus()));
    }


    @Test
    public void testFindBookedFlightsByFlightNumber_Fail_NoFlightFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findBookedFlightsByFlightNumber("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Add more test cases for other methods as needed
}
