package com.su.FlightScheduler.ControllerTest;

import com.su.FlightScheduler.APIs.PassengerAPI.PassengerController;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController passengerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GetPassengers() {
        List<PassengerEntity> passengerList = Arrays.asList(new PassengerEntity(), new PassengerEntity());
        when(passengerService.findAllPassengers()).thenReturn(passengerList);

        ResponseEntity<List<PassengerEntity>> response = passengerController.getPassengers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerList, response.getBody());
    }

    /*
    @Test
    public void GetPassengerWithId_Existing() {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setId("1");
        userDataDTO.setUserType("Passenger");
        when(passengerService.findPassengerById(1)).thenReturn(passengerEntity);
        ResponseEntity<Object> response = passengerController.getPassengerWithId(1);

        UserDataDTO returnedDto = (UserDataDTO) response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO.getId(), returnedDto.getId());
        assertEquals(userDataDTO.getUserType(), returnedDto.getUserType());
    }

     */

    @Test
    public void GetPassengerWithId_NonExisting() {
        when(passengerService.findPassengerById(1)).thenThrow(new RuntimeException("Passenger not found"));

        ResponseEntity<Object> response = passengerController.getPassengerWithId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Passenger not found", response.getBody());
    }

    @Test
    public void PostPassenger() {
        PassengerEntity passengerEntity = new PassengerEntity();
        when(passengerService.savePassenger(passengerEntity)).thenReturn(passengerEntity);

        ResponseEntity<Object> response = passengerController.postPassenger(passengerEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerEntity, response.getBody());
    }

    @Test
    public void PostPassenger_ExistingId() {
        PassengerEntity passengerEntity = new PassengerEntity();
        when(passengerService.savePassenger(passengerEntity)).thenThrow(new RuntimeException("Method not allowed"));

        ResponseEntity<Object> response = passengerController.postPassenger(passengerEntity);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

    @Test
    public void UpdatePassengerWithId() {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        PassengerEntity updatedPassengerEntity = new PassengerEntity();
        updatedPassengerEntity.setPassengerId(1);
        when(passengerService.findPassengerById(1)).thenReturn(passengerEntity);
        when(passengerService.updatePassenger(passengerEntity)).thenReturn(updatedPassengerEntity);

        ResponseEntity<Object> response = passengerController.updatePassengerWithId(1, passengerEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPassengerEntity, response.getBody());
    }

    @Test
    public void UpdatePassengerWithId_NonExisting() {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        when(passengerService.findPassengerById(1)).thenThrow(new RuntimeException("Passenger not found"));

        ResponseEntity<Object> response = passengerController.updatePassengerWithId(1, passengerEntity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Passenger not found", response.getBody());
    }

    @Test
    public void DeletePassengerWithId() {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);
        when(passengerService.deletePassengerById(1)).thenReturn(passengerEntity);

        ResponseEntity<Object> response = passengerController.deletePassengerWithId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerEntity, response.getBody());
    }

    @Test
    public void DeletePassengerWithId_NonExisting() {
        when(passengerService.deletePassengerById(1)).thenThrow(new RuntimeException("Passenger not found"));

        ResponseEntity<Object> response = passengerController.deletePassengerWithId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Passenger not found", response.getBody());
    }

    @Test
    public void PassengerLogin_Authorized() {
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        when(passengerService.authenticate(loginRequest)).thenReturn(true);

        ResponseEntity<Object> response = passengerController.passengerLogin(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginRequest, response.getBody());
    }

    @Test
    public void PassengerLogin_UnAuthorized() {
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        when(passengerService.authenticate(loginRequest)).thenReturn(false);

        ResponseEntity<Object> response = passengerController.passengerLogin(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}