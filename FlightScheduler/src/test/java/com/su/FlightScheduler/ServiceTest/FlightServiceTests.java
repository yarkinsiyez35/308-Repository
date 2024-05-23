package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.*;
import com.su.FlightScheduler.Service.FlightServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTests {

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

    @InjectMocks
    private FlightServiceImp flightService;

    private FlightEntity flight;
    private AirportEntity sourceAirport;
    private AirportEntity destinationAirport;
    private PlaneEntity plane;
    private CompanyEntity company;
    private AdminEntity admin;
    private VehicleTypeEntity vehicleType;

    @BeforeEach
    void setUp() {
        sourceAirport = new AirportEntity();
        sourceAirport.setAirportCode("JFK");

        destinationAirport = new AirportEntity();
        destinationAirport.setAirportCode("LAX");

        vehicleType = new VehicleTypeEntity();
        vehicleType.setVehicleType("Boeing737");
        vehicleType.setSeatingPlan("2|2*10=2|2*40"); // encoded seating plan

        plane = new PlaneEntity();
        plane.setPlaneId(1);
        plane.setVehicleType(vehicleType);

        company = new CompanyEntity();
        company.setCompanyName("AirlineCompany");

        admin = new AdminEntity();
        admin.setAdminId(1);

        flight = new FlightEntity();
        flight.setFlightNumber("FL123");
        flight.setSourceAirport(sourceAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setPlane(plane);
        flight.setDepartureDateTime(LocalDateTime.now().plusDays(1));
        flight.setLandingDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        flight.setFlightRange(3000);
        flight.setSharedFlight(false);
        flight.setSharedFlightCompany(company);
    }

    @Test
    void testGetFlightOrThrow_FlightFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        FlightEntity found = flightService.getFlightOrThrow("FL123");
        assertNotNull(found);
        assertEquals("FL123", found.getFlightNumber());
    }

    @Test
    void testGetFlightOrThrow_FlightNotFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> flightService.getFlightOrThrow("FL123"));
    }

    @Test
    void testSaveFlightObj() {
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity saved = flightService.saveFlightObj(flight);
        assertNotNull(saved);
        assertEquals("FL123", saved.getFlightNumber());
    }

    @Test
    void testCreateFlightFilled() {
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity created = flightService.createFlightFilled("FL123", "Info", sourceAirport, destinationAirport, plane, 3000, LocalDateTime.now(), LocalDateTime.now().plusHours(5), false, company, admin, "Menu");
        assertNotNull(created);
        assertEquals("FL123", created.getFlightNumber());
    }

    @Test
    void testCreateFlight() {
        FlightEntity newFlight = new FlightEntity();
        newFlight.setFlightNumber("FL123");
        newFlight.setFlightInfo("Info");
        newFlight.setAdmin(admin);

        when(flightRepository.save(any(FlightEntity.class))).thenReturn(newFlight);

        FlightEntity created = flightService.createFlight("FL123", "Info", admin);
        assertNotNull(created);
        assertEquals("FL123", created.getFlightNumber());
        assertEquals("Info", created.getFlightInfo());
        assertNotNull(created.getAdmin());
        assertEquals(admin.getAdminId(), created.getAdmin().getAdminId());
    }

    @Test
    void testAddFlightParams1() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.addFlightParams1("FL123", plane, sourceAirport, destinationAirport);
        assertNotNull(updated);
        assertEquals("FL123", updated.getFlightNumber());
    }

    @Test
    void testAddFlightParams2() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.addFlightParams2("FL123", 3000, LocalDateTime.now(), LocalDateTime.now().plusHours(5));
        assertNotNull(updated);
        assertEquals(3000, updated.getFlightRange());
    }

    @Test
    void testAddFlightParams3() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.addFlightParams3("FL123", true, company);
        assertNotNull(updated);
        assertTrue(updated.isSharedFlight());
    }

    @Test
    void testFindFlightByNumber_FlightFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        Optional<FlightEntity> found = flightService.findFlightByNumber("FL123");
        assertTrue(found.isPresent());
        assertEquals("FL123", found.get().getFlightNumber());
    }

    @Test
    void testFindFlightByNumber_FlightNotFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> flightService.findFlightByNumber("FL123"));
    }

    @Test
    void testFindAllFlights() {
        when(flightRepository.findAll()).thenReturn(Collections.singletonList(flight));
        List<FlightEntity> flights = flightService.findAllFlights();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    void testFindFlightsByDepartureAirport_FlightsFound() {
        when(flightRepository.findBySourceAirportAirportCode(anyString())).thenReturn(Collections.singletonList(flight));
        List<FlightEntity> flights = flightService.findFlightsByDepartureAirport("JFK");
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    void testFindFlightsByDepartureAirport_FlightsNotFound() {
        when(flightRepository.findBySourceAirportAirportCode(anyString())).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> flightService.findFlightsByDepartureAirport("JFK"));
    }

    @Test
    void testFindFlightsByDestinationAirport_FlightsFound() {
        when(flightRepository.findByDestinationAirportAirportCode(anyString())).thenReturn(Collections.singletonList(flight));
        List<FlightEntity> flights = flightService.findFlightsByDestinationAirport("LAX");
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    void testFindFlightsByDestinationAirport_FlightsNotFound() {
        when(flightRepository.findByDestinationAirportAirportCode(anyString())).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> flightService.findFlightsByDestinationAirport("LAX"));
    }

    @Test
    void testDeleteFlightByNumber() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        doNothing().when(flightRepository).deleteById(anyString());
        flightService.deleteFlightByNumber("FL123");
        verify(flightRepository, times(1)).deleteById("FL123");
    }

    @Test
    void testUpdateFlightInfo() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateFlightInfo("FL123", "Updated Info");
        assertNotNull(updated);
        assertEquals("Updated Info", updated.getFlightInfo());
    }

    @Test
    void testUpdateSourceAirport() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateSourceAirport("FL123", sourceAirport);
        assertNotNull(updated);
        assertEquals("JFK", updated.getSourceAirport().getAirportCode());
    }

    @Test
    void testUpdateDestinationAirport() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateDestinationAirport("FL123", destinationAirport);
        assertNotNull(updated);
        assertEquals("LAX", updated.getDestinationAirport().getAirportCode());
    }

    @Test
    void testUpdatePlane() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updatePlane("FL123", plane);
        assertNotNull(updated);
        assertEquals(1, updated.getPlane().getPlaneId());
    }

    @Test
    void testUpdateFlightRange() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateFlightRange("FL123", 5000);
        assertNotNull(updated);
        assertEquals(5000, updated.getFlightRange());
    }

    @Test
    void testUpdateDepartureDateTime() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(2);
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateDepartureDateTime("FL123", newDateTime);
        assertNotNull(updated);
        assertEquals(newDateTime, updated.getDepartureDateTime());
    }

    @Test
    void testUpdateLandingDateTime() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(2).plusHours(5);
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateLandingDateTime("FL123", newDateTime);
        assertNotNull(updated);
        assertEquals(newDateTime, updated.getLandingDateTime());
    }

    @Test
    void testUpdateSharedFlight() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateSharedFlight("FL123", true);
        assertNotNull(updated);
        assertTrue(updated.isSharedFlight());
    }

    @Test
    void testUpdateSharedFlightCompany() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateSharedFlightCompany("FL123", company);
        assertNotNull(updated);
        assertEquals("AirlineCompany", updated.getSharedFlightCompany().getCompanyName());
    }

    @Test
    void testUpdateStandardMenu() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flight);
        FlightEntity updated = flightService.updateStandardMenu("FL123", "New Menu");
        assertNotNull(updated);
        assertEquals("New Menu", updated.getStandardMenu());
    }

    @Test
    void testGetSourceAirport() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        AirportEntity found = flightService.getSourceAirport("FL123");
        assertNotNull(found);
        assertEquals("JFK", found.getAirportCode());
    }

    @Test
    void testGetDestinationAirport() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        AirportEntity found = flightService.getDestinationAirport("FL123");
        assertNotNull(found);
        assertEquals("LAX", found.getAirportCode());
    }

    @Test
    void testGetPlane() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        PlaneEntity found = flightService.getPlane("FL123");
        assertNotNull(found);
        assertEquals(1, found.getPlaneId());
    }

    @Test
    void testGetCompany() {
        flight.setSharedFlightCompany(company);
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        CompanyEntity found = flightService.getCompany("FL123");
        assertNotNull(found);
        assertEquals("AirlineCompany", found.getCompanyName());
    }

    @Test
    void testGetDateTime() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        LocalDateTime found = flightService.getDateTime("FL123");
        assertNotNull(found);
        assertEquals(flight.getDepartureDateTime(), found);
    }

    @Test
    void testFindSeatingPlanByFlightNumber_FlightFound() {
        VehicleTypeEntity vehicleType = flight.getPlane().getVehicleType();
        VehicleTypeRepository.SeatingPlanProjection projection = mock(VehicleTypeRepository.SeatingPlanProjection.class);
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType(anyString(), eq(VehicleTypeRepository.SeatingPlanProjection.class))).thenReturn(projection);
        when(projection.getSeatingPlan()).thenReturn("2|2*10=2|2*40");
        VehicleTypeRepository.SeatingPlanProjection seatingPlan = flightService.findSeatingPlanByFlightNumber("FL123");
        assertNotNull(seatingPlan);
        assertEquals("2|2*10=2|2*40", seatingPlan.getSeatingPlan());
    }

    @Test
    void testFindSeatingPlanByFlightNumber_FlightNotFound() {
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> flightService.findSeatingPlanByFlightNumber("FL123"));
    }

    @Test
    void testFindAttendeeCapacityByFlightNumber_FlightFound() {
        VehicleTypeEntity vehicleType = flight.getPlane().getVehicleType();
        VehicleTypeRepository.AttendeeCapacityProjection projection = mock(VehicleTypeRepository.AttendeeCapacityProjection.class);
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType(anyString(), eq(VehicleTypeRepository.AttendeeCapacityProjection.class))).thenReturn(projection);
        VehicleTypeRepository.AttendeeCapacityProjection attendeeCapacity = flightService.findAttendeeCapacityByFlightNumber("FL123");
        assertNotNull(attendeeCapacity);
    }

    @Test
    void testFindAttendeeCapacityByFlightNumber_FlightNotFound() {
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> flightService.findAttendeeCapacityByFlightNumber("FL123"));
    }

    @Test
    void testFindPilotCapacityByFlightNumber_FlightFound() {
        VehicleTypeEntity vehicleType = flight.getPlane().getVehicleType();
        VehicleTypeRepository.PilotCapacityProjection projection = mock(VehicleTypeRepository.PilotCapacityProjection.class);
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType(anyString(), eq(VehicleTypeRepository.PilotCapacityProjection.class))).thenReturn(projection);
        VehicleTypeRepository.PilotCapacityProjection pilotCapacity = flightService.findPilotCapacityByFlightNumber("FL123");
        assertNotNull(pilotCapacity);
    }

    @Test
    void testFindPilotCapacityByFlightNumber_FlightNotFound() {
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> flightService.findPilotCapacityByFlightNumber("FL123"));
    }

    @Test
    void testFindFlightDetailsByFlightNumber_FlightFound() {
        FlightRepository.FlightDetailsProjection projection = mock(FlightRepository.FlightDetailsProjection.class);
        when(flightRepository.findFlightDetailsByFlightNumber(anyString())).thenReturn(projection);
        FlightRepository.FlightDetailsProjection details = flightService.findFlightDetailsByFlightNumber("FL123");
        assertNotNull(details);
    }

    @Test
    void testFindFlightDetailsByFlightNumber_FlightNotFound() {
        when(flightRepository.findFlightDetailsByFlightNumber(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> flightService.findFlightDetailsByFlightNumber("FL123"));
    }

    @Test
    void testDecodeSeatingPlan_FlightFound() {
        VehicleTypeEntity vehicleType = flight.getPlane().getVehicleType();
        VehicleTypeRepository.SeatingPlanProjection projection = mock(VehicleTypeRepository.SeatingPlanProjection.class);
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType(anyString(), eq(VehicleTypeRepository.SeatingPlanProjection.class))).thenReturn(projection);
        when(projection.getSeatingPlan()).thenReturn("2|2*10=2|2*40");

        List<SeatingTypeDTO> seatingList = flightService.decodeSeatingPlan("FL123");
        assertNotNull(seatingList);
        assertEquals(2, seatingList.size());
        assertEquals(2, seatingList.get(0).getStartRow());
        assertEquals(2, seatingList.get(0).getEndRow());
        assertEquals("10", seatingList.get(0).getColumns());
        assertEquals(2, seatingList.get(1).getStartRow());
        assertEquals(2, seatingList.get(1).getEndRow());
        assertEquals("40", seatingList.get(1).getColumns());
    }

    @Test
    void testDecodeSeatingPlan_FlightNotFound() {
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> flightService.decodeSeatingPlan("FL123"));
    }

    @Test
    void testFindBookedFlightsByFlightNumber_FlightFoundAndPassengersFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setSeatNumber("1A");
        PassengerEntity passenger = new PassengerEntity();
        passenger.setPassengerId(1);
        passengerFlight.setPassenger(passenger);

        VehicleTypeEntity vehicleType = flight.getPlane().getVehicleType();
        VehicleTypeRepository.SeatingPlanProjection projection = mock(VehicleTypeRepository.SeatingPlanProjection.class);
        when(flightRepository.findVehicleTypeByFlightId(anyString())).thenReturn(vehicleType);
        when(vehicleTypeRepository.findByVehicleType(anyString(), eq(VehicleTypeRepository.SeatingPlanProjection.class))).thenReturn(projection);
        when(projection.getSeatingPlan()).thenReturn("2|2*10=2|2*40");

        when(passengerFlightRepository.findPassengerFlightByFlight(any(FlightEntity.class))).thenReturn(Collections.singletonList(passengerFlight));

        List<SeatingDTO> bookedSeats = flightService.findBookedFlightsByFlightNumber("FL123");
        assertNotNull(bookedSeats);
        assertFalse(bookedSeats.isEmpty());
        assertEquals("1A", bookedSeats.get(0).getSeatPosition());
        assertEquals(1, bookedSeats.get(0).getUserId());
    }

    @Test
    void testFindBookedFlightsByFlightNumber_FlightFoundAndPassengersNotFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(flight));
        when(passengerFlightRepository.findPassengerFlightByFlight(any(FlightEntity.class))).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> flightService.findBookedFlightsByFlightNumber("FL123"));
    }

    @Test
    void testFindBookedFlightsByFlightNumber_FlightNotFound() {
        when(flightRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> flightService.findBookedFlightsByFlightNumber("FL123"));
    }
}
