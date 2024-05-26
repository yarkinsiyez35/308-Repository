package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

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



    // -- Create Flight Tests --
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
    public void testCreateFlight_Success_AutoFlightNumber() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("auto");

        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);
        when(flightRepository.findById(anyString())).thenReturn(Optional.empty());

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
    public void testCreateFlight_Fail_LandingAirportNotFound() {
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, airports are wrong!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testCreateFlight_Fail_FlightNumberAlreadyExists() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.createFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not create flight, flight number already exists!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // -- End of Create Flight Tests --


    // ---------------------------------------------------------


    // -- Get Flight Tests --
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
    public void testFindFlightByNumber_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        Optional<FlightEntity> flight = flightService.findFlightByNumber("SU1234");

        assertTrue(flight.isPresent());
        assertEquals("SU1234", flight.get().getFlightNumber());
    }

    @Test
    public void testFindFlightByNumber_NotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Optional<FlightEntity> flight = flightService.findFlightByNumber("SU1234");

        assertFalse(flight.isPresent());
    }

    @Test
    public void testFindFlightByNumberDTO_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        FlightDataDTO flightDataDTO = flightService.findFlightByNumberDTO("SU1234");

        assertNotNull(flightDataDTO);
        assertEquals("SU1234", flightDataDTO.getFlightId());
    }

    @Test
    public void testFindFlightByNumberDTO_NotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightByNumberDTO("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindAllFlightsDTO_Success() {
        when(flightRepository.findAll()).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flightDataDTOList = flightService.findAllFlightsDTO();

        assertNotNull(flightDataDTOList);
        assertEquals(1, flightDataDTOList.size());
        assertEquals("SU1234", flightDataDTOList.get(0).getFlightId());
    }

    @Test
    public void testFindAllFlightsDTO_EmptyList() {
        when(flightRepository.findAll()).thenReturn(List.of());

        List<FlightDataDTO> flightDataDTOList = flightService.findAllFlightsDTO();

        assertNotNull(flightDataDTOList);
        assertTrue(flightDataDTOList.isEmpty());
    }

    @Test
    public void testFindFlightsByDepartureAirportAndDepartureDateTime_Success() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(1);
        when(flightRepository.findBySourceAirportAirportCodeAndDepartureDateTime("IST", departureDateTime)).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flightDataDTOList = flightService.findFlightsByDepartureAirportAndDepartureDateTime("IST", departureDateTime);

        assertNotNull(flightDataDTOList);
        assertEquals(1, flightDataDTOList.size());
        assertEquals("SU1234", flightDataDTOList.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAirportAndDepartureDateTime_NotFound() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(1);
        when(flightRepository.findBySourceAirportAirportCodeAndDepartureDateTime("IST", departureDateTime)).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureAirportAndDepartureDateTime("IST", departureDateTime);
        });

        String expectedMessage = "No flights found for the given departure airport and departure date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDestinationAirportAndLandingDateTime_Success() {
        LocalDateTime landingDateTime = LocalDateTime.now().plusDays(1);
        when(flightRepository.findByDestinationAirportAirportCodeAndLandingDateTime("ESB", landingDateTime)).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flightDataDTOList = flightService.findFlightsByDestinationAirportAndLandingDateTime("ESB", landingDateTime);

        assertNotNull(flightDataDTOList);
        assertEquals(1, flightDataDTOList.size());
        assertEquals("SU1234", flightDataDTOList.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDestinationAirportAndLandingDateTime_NotFound() {
        LocalDateTime landingDateTime = LocalDateTime.now().plusDays(1);
        when(flightRepository.findByDestinationAirportAirportCodeAndLandingDateTime("ESB", landingDateTime)).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDestinationAirportAndLandingDateTime("ESB", landingDateTime);
        });

        String expectedMessage = "No flights found for the given destination airport and landing date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime_Success() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime landingDateTime = LocalDateTime.now().plusDays(1).plusHours(2);
        when(flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(
                "IST", "ESB", departureDateTime, landingDateTime)).thenReturn(List.of(flightEntity));

        List<FlightDataDTO> flightDataDTOList = flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(
                "IST", "ESB", departureDateTime, landingDateTime);

        assertNotNull(flightDataDTOList);
        assertEquals(1, flightDataDTOList.size());
        assertEquals("SU1234", flightDataDTOList.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime_NotFound() {
        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime landingDateTime = LocalDateTime.now().plusDays(1).plusHours(2);
        when(flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(
                "IST", "ESB", departureDateTime, landingDateTime)).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(
                    "IST", "ESB", departureDateTime, landingDateTime);
        });

        String expectedMessage = "No flights found for the given departure and destination airports and departure and landing date and time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    // -- End of Get Flight Tests --

    // ---------------------------------------------------------

    // -- Update Flight Tests --
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
    public void testUpdateFlightByFlightDTO_Success() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        // Mock the necessary repository calls
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));

        FlightEntity updatedFlight = flightService.updateFlightByFlightDTO(flightDataDTO, 316962);

        assertNotNull(updatedFlight);
        assertEquals("SU1234", updatedFlight.getFlightNumber());
        assertEquals(sourceAirport, updatedFlight.getSourceAirport());
        assertEquals(destinationAirport, updatedFlight.getDestinationAirport());
        assertEquals(plane, updatedFlight.getPlane());
        assertEquals("Standard menu", updatedFlight.getStandardMenu());
    }


    @Test
    public void testUpdateFlightByFlightDTO_AdminsDoNotMatch() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        AdminEntity differentAdmin = new AdminEntity();
        differentAdmin.setAdminId(999999);
        flightEntity.setAdmin(differentAdmin);

        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlightByFlightDTO(flightDataDTO, 316962);
        });

        String expectedMessage = "Admins do not match!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateSourceAirport_NullSourceAirport() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateSourceAirport("SU1234", null);
        });

        String expectedMessage = "Source airport cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateDestinationAirport_NullDestinationAirport() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateDestinationAirport("SU1234", null);
        });

        String expectedMessage = "Destination airport cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testUpdatePlane_NullPlane() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updatePlane("SU1234", null);
        });

        String expectedMessage = "Plane cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlightRange_InvalidRange() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlightRange("SU1234", 0);
        });

        String expectedMessage = "Flight range must be greater than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateDepartureDateTime_InvalidTime() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateDepartureDateTime("SU1234", LocalDateTime.now().minusDays(1));
        });

        String expectedMessage = "Departure time cannot be in the past or null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateLandingDateTime_InvalidTime() {
        flightEntity.setDepartureDateTime(LocalDateTime.now().plusDays(1).plusHours(1));
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateLandingDateTime("SU1234", LocalDateTime.now().plusDays(1));
        });

        String expectedMessage = "Landing time cannot be before departure time or null";
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
    public void testUpdateSharedFlightCompany_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateSharedFlightCompany("SU1234", company);

        assertNotNull(updatedFlight);
        assertEquals(company, updatedFlight.getSharedFlightCompany());
    }

    @Test
    public void testUpdateSharedFlightCompany_NullCompany() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateSharedFlightCompany("SU1234", null);
        });

        String expectedMessage = "Shared flight company cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateStandardMenu_InvalidMenu() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateStandardMenu("SU1234", "");
        });

        String expectedMessage = "Standard menu cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    // -- End of Update Flight Tests --

    // ---------------------------------------------------------

    // -- Delete Flight Tests --
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
    // -- End of Delete Flight Tests --

    // ---------------------------------------------------------

    // -- Getter Tests --
    @Test
    public void testGetSourceAirport_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        AirportEntity sourceAirport = flightService.getSourceAirport("SU1234");

        assertNotNull(sourceAirport);
        assertEquals("IST", sourceAirport.getAirportCode());
    }

    @Test
    public void testGetSourceAirport_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getSourceAirport("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetDestinationAirport_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        AirportEntity destinationAirport = flightService.getDestinationAirport("SU1234");

        assertNotNull(destinationAirport);
        assertEquals("ESB", destinationAirport.getAirportCode());
    }

    @Test
    public void testGetDestinationAirport_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getDestinationAirport("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetPlane_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        PlaneEntity plane = flightService.getPlane("SU1234");

        assertNotNull(plane);
        assertEquals(3169, plane.getPlaneId());
    }

    @Test
    public void testGetPlane_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getPlane("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetCompany_Success() {
        flightEntity.setSharedFlightCompany(company);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        CompanyEntity company = flightService.getCompany("SU1234");

        assertNotNull(company);
        assertEquals("TestAirlines", company.getCompanyName());
    }

    @Test
    public void testGetCompany_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getCompany("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetDateTime_Success() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        LocalDateTime departureDateTime = flightService.getDateTime("SU1234");

        assertNotNull(departureDateTime);
        assertEquals(flightEntity.getDepartureDateTime(), departureDateTime);
    }

    @Test
    public void testGetDateTime_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getDateTime("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    // -- End of Getter Tests --

    // ---------------------------------------------------------

    // -- Find Seating Plan Tests --
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


    /*@Test
    public void testGetUsersDTOByFlightNumber_Success() {
        // Arrange
        String flightNumber = "SU1234";
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber(flightNumber);

        PassengerEntity passenger = new PassengerEntity();
        passenger.setPassengerId(1234);
        passenger.setEmail("email@gmail.com");
        passenger.setPassword("password");
        passenger.setFirstName("John");
        passenger.setSurname("Doe");
        passenger.setGender("Male");
        passenger.setNationality("USA");

        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setFlight(flight);
        passengerFlight.setPassenger(passenger);

        List<PassengerFlight> passengerFlights = List.of(passengerFlight);

        // Mocking the flight repository to return the flight entity
        when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(flight));

        // Mocking the flightService's getFlightOrThrow method to return the flight entity
        doReturn(flight).when(flightService).getFlightOrThrow(flightNumber);

        // Mocking the passengerFlightRepository to return the list of passenger flights
        when(passengerFlightRepository.findPassengerFlightByFlight(flight)).thenReturn(passengerFlights);

        // Mocking the UserDataDTOFactory to return a valid UserDataDTO
        UserDataDTO userDataDTO = new UserDataDTO("email@gmail.com", null, "John", "Doe", "1234", 30, "Male", "USA", null, null, null, null, null);
        when(UserDataDTOFactory.create_passenger_with_passenger_flight(passengerFlight)).thenReturn(userDataDTO);

        // Act
        List<UserDataDTO> users = flightService.getUsersDTOByFlightNumber(flightNumber);

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("email@gmail.com", users.get(0).getEmail());
        assertEquals("John", users.get(0).getName());
        assertEquals("Doe", users.get(0).getSurname());
        assertEquals("Male", users.get(0).getGender());
        assertEquals("USA", users.get(0).getNationality());
    }*/




    @Test
    public void testGetUsersDTOByFlightNumber_FlightNumberNullOrEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.getUsersDTOByFlightNumber(null);
        });

        String expectedMessage = "Flight number cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.getUsersDTOByFlightNumber("");
        });

        actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetUsersDTOByFlightNumber_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getUsersDTOByFlightNumber("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetUsersDTOByFlightNumber_NoPassengersFound() {
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("SU1234");

        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flight));
        when(passengerFlightRepository.findPassengerFlightByFlight(flight)).thenReturn(List.of());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getUsersDTOByFlightNumber("SU1234");
        });

        String expectedMessage = "No passengers found for the given flight";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetUsersDTOByFlightNumber_PassengerNotFound() {
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("SU1234");

        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setFlight(flight);
        passengerFlight.setPassenger(null);

        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flight));
        when(passengerFlightRepository.findPassengerFlightByFlight(flight)).thenReturn(List.of(passengerFlight));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getUsersDTOByFlightNumber("SU1234");
        });

        String expectedMessage = "Passenger not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetAvailableSeats_Success() {
        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("SU1234");

        SeatingDTO bookedSeat = new SeatingDTO();
        bookedSeat.setSeatPosition("1A");
        bookedSeat.setStatus(true);

        SeatingDTO availableSeat = new SeatingDTO();
        availableSeat.setSeatPosition("1B");
        availableSeat.setStatus(false);

        // Mocking getFlightOrThrow to return the flight entity
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flight));

        // Using spy to partially mock the flightService
        FlightServiceImp spyFlightService = Mockito.spy(flightService);

        // Mocking findBookedFlightsByFlightNumber to return booked and available seats
        List<SeatingDTO> seatingList = new ArrayList<>();
        seatingList.add(bookedSeat);
        seatingList.add(availableSeat);

        doReturn(seatingList).when(spyFlightService).findBookedFlightsByFlightNumber("SU1234");

        List<String> availableSeats = spyFlightService.getAvailableSeats("SU1234");

        assertNotNull(availableSeats);
        assertEquals(1, availableSeats.size());
        assertEquals("1B", availableSeats.get(0));
    }


    @Test
    public void testGetAvailableSeats_FlightNotFound() {
        when(flightRepository.findById("SU1234")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            flightService.getAvailableSeats("SU1234");
        });

        String expectedMessage = "Flight not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Add more test cases for other methods as needed

    // New updateFlightMethodTest

    // New
    @Test
    public void testUpdateFlight_Success() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightEntity updatedFlight = flightService.updateFlight(flightDataDTO, 316962);

        assertNotNull(updatedFlight);
        assertEquals("SU1234", updatedFlight.getFlightNumber());
        assertEquals(sourceAirport, updatedFlight.getSourceAirport());
        assertEquals(destinationAirport, updatedFlight.getDestinationAirport());
        assertEquals(plane, updatedFlight.getPlane());
        assertEquals(admin, updatedFlight.getAdmin());
    }

    @Test
    public void testUpdateFlight_Fail_FlightNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, id does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_SourceAirportNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, airports are wrong!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_LandingAirportNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, airports are wrong!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_PlaneNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, plane id does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testUpdateFlight_Fail_DepartureTimeInThePast() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");
        flightDataDTO.setDepartureTime(LocalDateTime.now().minusHours(1));

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Departure time cannot be in the past";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_LandingTimeBeforeDepartureTime() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");
        flightDataDTO.setDepartureTime(LocalDateTime.now().plusHours(2));
        flightDataDTO.setLandingTime(LocalDateTime.now().plusHours(1));

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Landing time cannot be before departure time";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_CompanyNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.empty());
        when(adminRepository.findById(316962)).thenReturn(Optional.of(admin));

        flightDataDTO.setAirlineCompany("TestAirlines");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, the company does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFlight_Fail_AdminNotFound() {
        FlightDataDTO flightDataDTO = this.flightDataDTO;
        flightDataDTO.setFlightId("SU1234");

        when(flightRepository.existsById("SU1234")).thenReturn(true);
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));
        when(airportRepository.findAirportEntityByAirportCode("IST")).thenReturn(Optional.of(sourceAirport));
        when(airportRepository.findAirportEntityByAirportCode("ESB")).thenReturn(Optional.of(destinationAirport));
        when(planeRepository.findById("3169")).thenReturn(Optional.of(plane));
        when(companyRepository.findById("TestAirlines")).thenReturn(Optional.of(company));
        when(adminRepository.findById(316962)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(flightDataDTO, 316962);
        });

        String expectedMessage = "Could not update flight, admin does not exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
