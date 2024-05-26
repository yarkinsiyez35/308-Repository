package com.su.FlightScheduler.ControllerTest;


import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.APIs.MainSystemAPI.MainSystemController;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Service.AttendantAssignmentService;
import com.su.FlightScheduler.Service.PassengerFlightService;
import com.su.FlightScheduler.Service.PilotFlightAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;

class MainSystemControllerTest {

    @Mock
    private PilotFlightAssignmentService pilotFlightAssignmentService;

    @Mock
    private AttendantAssignmentService attendantAssignmentService;

    @Mock
    private PassengerFlightService passengerFlightService;

    @Mock
    private UserDataDTOFactory userDataDTOFactory;

    @InjectMocks
    private MainSystemController mainSystemController;

    private UserDataDTO userDataDTO;
    private List<UserDataDTO> userDataDTOList;
    private List<PassengerFlight> passengerFlights;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDataDTO = new UserDataDTO();
        userDataDTOList = Collections.singletonList(userDataDTO);
        passengerFlights = Collections.singletonList(new PassengerFlight());
    }

    @Test
    void testGetFlightsOfPilot() {
        when(pilotFlightAssignmentService.getFlightsOfPilot(anyInt())).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.getFlightsOfPilot(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

    @Test
    void testGetFlightsOfPilot_NotFound() {
        when(pilotFlightAssignmentService.getFlightsOfPilot(anyInt())).thenThrow(new RuntimeException("Pilot not found"));
        ResponseEntity<Object> response = mainSystemController.getFlightsOfPilot(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pilot not found", response.getBody());
    }

    @Test
    void testGetAvailablePilotsForFlight() {
        when(pilotFlightAssignmentService.getAvailablePilotsForFlight(anyString())).thenReturn(userDataDTOList);
        ResponseEntity<Object> response = mainSystemController.getAvailablePilotsForFlight("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTOList, response.getBody());
    }

    @Test
    void testGetAvailablePilotsForFlight_NotFound() {
        when(pilotFlightAssignmentService.getAvailablePilotsForFlight(anyString())).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<Object> response = mainSystemController.getAvailablePilotsForFlight("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testGetPilotsOfFlight() {
        when(pilotFlightAssignmentService.getPilotsOfFlight(anyString())).thenReturn(userDataDTOList);
        ResponseEntity<Object> response = mainSystemController.getPilotsOfFlight("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTOList, response.getBody());
    }

    @Test
    void testGetPilotsOfFlight_NotFound() {
        when(pilotFlightAssignmentService.getPilotsOfFlight(anyString())).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<Object> response = mainSystemController.getPilotsOfFlight("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testAssignPilotToFlight() {
        when(pilotFlightAssignmentService.assignPilotToFlight(anyString(), anyInt())).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.assignPilotToFlight(1, "FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

    @Test
    void testAssignPilotToFlight_MethodNotAllowed() {
        when(pilotFlightAssignmentService.assignPilotToFlight(anyString(), anyInt())).thenThrow(new RuntimeException("Method not allowed"));
        ResponseEntity<Object> response = mainSystemController.assignPilotToFlight(1, "FL123");
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

    @Test
    void testRemovePilotFromFlight() {
        when(pilotFlightAssignmentService.removeFlightFromAPilot(anyString(), anyInt())).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.removePilotFromFlight(1, "FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

    @Test
    void testRemovePilotFromFlight_MethodNotAllowed() {
        when(pilotFlightAssignmentService.removeFlightFromAPilot(anyString(), anyInt())).thenThrow(new RuntimeException("Method not allowed"));
        ResponseEntity<Object> response = mainSystemController.removePilotFromFlight(1, "FL123");
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

    @Test
    void testGetFlightAssignmentsOfAttendant() {
        when(attendantAssignmentService.getFlightsOfAttendant(anyInt())).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.getFlightAssignmentsOfAttendant(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

    @Test
    void testGetFlightAssignmentsOfAttendant_NotFound() {
        when(attendantAssignmentService.getFlightsOfAttendant(anyInt())).thenThrow(new RuntimeException("Attendant not found"));
        ResponseEntity<Object> response = mainSystemController.getFlightAssignmentsOfAttendant(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Attendant not found", response.getBody());
    }

    @Test
    void testGetAvailableAttendantsForFlight() {
        when(attendantAssignmentService.getAvailableAttendantsForFlight(anyString())).thenReturn(userDataDTOList);
        ResponseEntity<Object> response = mainSystemController.getAvailableAttendantsForFlight("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTOList, response.getBody());
    }

    @Test
    void testGetAvailableAttendantsForFlight_NotFound() {
        when(attendantAssignmentService.getAvailableAttendantsForFlight(anyString())).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<Object> response = mainSystemController.getAvailableAttendantsForFlight("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testGetAttendantsOfFlight() {
        when(attendantAssignmentService.getAttendantsOfFlight(anyString())).thenReturn(userDataDTOList);
        ResponseEntity<Object> response = mainSystemController.getAttendantsOfFlight("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTOList, response.getBody());
    }

    @Test
    void testGetAttendantsOfFlight_NotFound() {
        when(attendantAssignmentService.getAttendantsOfFlight(anyString())).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<Object> response = mainSystemController.getAttendantsOfFlight("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

    @Test
    void testAssignAttendantToFlight() {
        when(attendantAssignmentService.assignAttendantToFlight(anyString(), anyInt())).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.assignAttendantToFlight(1, "FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

    @Test
    void testAssignAttendantToFlight_MethodNotAllowed() {
        when(attendantAssignmentService.assignAttendantToFlight(anyString(), anyInt())).thenThrow(new RuntimeException("Method not allowed"));
        ResponseEntity<Object> response = mainSystemController.assignAttendantToFlight(1, "FL123");
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

    /*
    @Test
    void testGetFlightsOfPassenger() {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);

        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setBookingId(1);
        List<PassengerFlight> passengerFlightList = new ArrayList<>();
        passengerFlightList.add(passengerFlight);

        passengerEntity.setPassengerFlights(passengerFlightList);

        UserDataDTO userDataDTO = new UserDataDTO(); // Create UserDataDTO properly
        userDataDTO.setFlights(passengerFlightList);

        when(passengerFlightService.findBookedFlightsByPassengerId(1)).thenReturn(userDataDTO);
        ResponseEntity<Object> response = mainSystemController.getFlightsOfPassenger(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

     */

    @Test
    void testGetFlightsOfPassenger_NotFound() {
        when(passengerFlightService.findBookedFlightsByPassengerId(anyInt())).thenThrow(new RuntimeException("Passenger not found"));
        ResponseEntity<Object> response = mainSystemController.getFlightsOfPassenger(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Passenger not found", response.getBody());
    }

/*
    @Test
    void testGetPassengersOfFlight() {
        when(passengerFlightService.findBookedFlightsByPassengerId(anyInt())).thenReturn(passengerFlights);
        ResponseEntity<Object> response = mainSystemController.getPassengersOfFlight("FL123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerFlights, response.getBody());
    }

 */

    /*
    @Test
    void testGetPassengersOfFlight_NotFound() {
        when(passengerFlightService.findBookedFlightsByPassengerId(anyInt())).thenThrow(new RuntimeException("Flight not found"));
        ResponseEntity<Object> response = mainSystemController.getPassengersOfFlight("FL123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Flight not found", response.getBody());
    }

     */

    /*
    @Test
    void testDeleteFlightFromPassenger() {
        System.out.println("Checking if mocks are properly configured...");

        // Create and set up PassengerEntity
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        passengerEntity.setEmail("john.doe@example.com"); // Assuming email is needed

        System.out.println("Verifying PassengerEntity setup...");

        // Create and set up PassengerFlight
        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setBookingId(1);
        passengerFlight.setPassenger(passengerEntity); // Ensure the passenger is set
        List<PassengerFlight> passengerFlightList = new ArrayList<>();
        passengerFlightList.add(passengerFlight);
        System.out.println("Verifying PassengerFlight setup...");
        System.out.println("Associating PassengerFlightList with PassengerEntity...");

        passengerEntity.setPassengerFlights(passengerFlightList);

        // Create and set up FlightEntity
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setSourceAirport("JFK"); // Set necessary fields
        flightEntity.setDestinationAirport("LAX");
        flightEntity.setFlightNumber("SU1234");

        // Mock the flight repository to return the flightEntity
        when(flightRepository.findById("SU1234")).thenReturn(Optional.of(flightEntity));

        System.out.println("Mocking flight repository to return flight entity...");

        // Mock the creation of UserDataDTO
        UserDataDTO userDataDTO = userDataDTOFactory.create_passenger_data_with_flight_list(passengerFlightList, passengerEntity);
        System.out.println("Creating UserDataDTO with the flight list and passenger entity...");

        // Mock the cancelFlight service call
        when(passengerFlightService.cancelFlight(anyInt())).thenReturn(passengerFlight);
        System.out.println("Mocking cancelFlight service call...");

        // Call the method under test
        ResponseEntity<Object> response = mainSystemController.deleteFlightFromPassenger(1, 1);
        System.out.println("Calling deleteFlightFromPassenger method on mainSystemController...");

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Asserting response status code is HttpStatus.OK...");

        assertEquals(userDataDTO, response.getBody());
        System.out.println("Asserting response body matches the expected UserDataDTO...");

        System.out.println("Final check: All assertions passed.");
    }




    @Test
    void testDeleteFlightFromPassenger_MethodNotAllowed() {

        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);

        when(passengerFlightService.cancelFlight(1)).thenThrow(new RuntimeException("Booking with id: 1 cannot be deleted!"));
        ResponseEntity<Object> response = mainSystemController.deleteFlightFromPassenger(1, 1);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Booking with id: 1 cannot be deleted!", response.getBody());
    }
    */



}
