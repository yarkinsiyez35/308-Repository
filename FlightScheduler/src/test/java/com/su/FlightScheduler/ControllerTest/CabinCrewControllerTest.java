package com.su.FlightScheduler.ControllerTest;

import com.su.FlightScheduler.APIs.CabinCrewAPI.CabinCrewController;
import com.su.FlightScheduler.DTO.CabinCrewDTOs.AttendantWithLanguagesAsStringDTO;
import com.su.FlightScheduler.DTO.CabinCrewDTOs.AttendantWithLanguagesDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Service.AttendantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class CabinCrewControllerTest {

    @Mock
    private AttendantService attendantService;

    @InjectMocks
    private CabinCrewController cabinCrewController;

    private CabinCrewEntity cabinCrew;
    private AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO;
    private UserDataDTO userDataDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cabinCrew = new CabinCrewEntity();
        cabinCrew.setAttendantId(1);
        cabinCrew.setEmail("email@gmail.com");
        cabinCrew.setPassword("password");
        cabinCrew.setFirstName("First");
        cabinCrew.setSurname("Last");
        cabinCrew.setAge(30);
        cabinCrew.setGender("male");
        cabinCrew.setNationality("Alien");
        cabinCrew.setSeniority("Senior");

        AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(cabinCrew);
        attendantWithLanguagesAsStringDTO = new AttendantWithLanguagesAsStringDTO(attendantWithLanguagesDTO);
        userDataDTO = UserDataDTOFactory.create_cabin_crew_with_cabin_crew_entity(cabinCrew);
    }

    @Test
    void testGetAllCabinCrew() {
        List<CabinCrewEntity> cabinCrewList = new ArrayList<>();
        cabinCrewList.add(cabinCrew);

        when(attendantService.findAllCabinCrew()).thenReturn(cabinCrewList);

        ResponseEntity<List<AttendantWithLanguagesAsStringDTO>> response = cabinCrewController.getAllCabinCrew();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /*
    @Test
    void testGetAttendantById() {
        when(attendantService.findAttendantById(anyInt())).thenReturn(cabinCrew);

        ResponseEntity<Object> response = cabinCrewController.getAttendantById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(attendantWithLanguagesAsStringDTO, response.getBody());
    }

     */

    @Test
    void testGetAttendantById_NotFound() {
        when(attendantService.findAttendantById(anyInt())).thenThrow(new RuntimeException("Attendant not found"));

        ResponseEntity<Object> response = cabinCrewController.getAttendantById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Attendant not found", response.getBody());
    }

    /*
    @Test
    void testCreateAttendantWithId() {
        when(attendantService.saveCabin(any(CabinCrewEntity.class))).thenReturn(cabinCrew);

        ResponseEntity<Object> response = cabinCrewController.createAttendantWithId(1, attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(attendantWithLanguagesAsStringDTO, response.getBody());
    }

     */

    @Test
    void testCreateAttendantWithId_MethodNotAllowed() {
        when(attendantService.saveCabin(any(CabinCrewEntity.class))).thenThrow(new RuntimeException("Method not allowed"));

        ResponseEntity<Object> response = cabinCrewController.createAttendantWithId(1, attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

    /*
    @Test
    void testCreateAttendantWithoutId() {
        when(attendantService.saveCabin(any(CabinCrewEntity.class))).thenReturn(cabinCrew);

        ResponseEntity<Object> response = cabinCrewController.createAttendantWithoutId(attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDataDTO, response.getBody());
    }

     */

    /*
    @Test
    void testCreateAttendantWithoutId_MethodNotAllowed() {
        when(attendantService.saveCabin(any(CabinCrewEntity.class))).thenThrow(new RuntimeException("Method not allowed"));

        ResponseEntity<Object> response = cabinCrewController.createAttendantWithoutId(attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody());
    }

     */

    /*
    @Test
    void testUpdateAttendantWithId() {
        when(attendantService.updateCabin(any(CabinCrewEntity.class))).thenReturn(cabinCrew);

        ResponseEntity<Object> response = cabinCrewController.updateAttendantWithId(1, attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(attendantWithLanguagesAsStringDTO, response.getBody());
    }

     */

    @Test
    void testUpdateAttendantWithId_NotFound() {
        when(attendantService.updateCabin(any(CabinCrewEntity.class))).thenThrow(new RuntimeException("Attendant not found"));

        ResponseEntity<Object> response = cabinCrewController.updateAttendantWithId(1, attendantWithLanguagesAsStringDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Attendant not found", response.getBody());
    }

    /*
    @Test
    void testDeleteAttendantById() {
        when(attendantService.deleteCabinById(anyInt())).thenReturn(cabinCrew);

        ResponseEntity<Object> response = cabinCrewController.deleteAttendantById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(attendantWithLanguagesAsStringDTO, response.getBody());
    }

     */

    @Test
    void testDeleteAttendantById_NotFound() {
        when(attendantService.deleteCabinById(anyInt())).thenThrow(new RuntimeException("Attendant not found"));

        ResponseEntity<Object> response = cabinCrewController.deleteAttendantById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Attendant not found", response.getBody());
    }

    @Test
    void testAttendantLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@gmail.com");
        loginRequest.setPassword("password");

        when(attendantService.authenticate(any(LoginRequest.class))).thenReturn(true);

        ResponseEntity<Object> response = cabinCrewController.attendantLogin(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginRequest, response.getBody());
    }

    @Test
    void testAttendantLogin_Unauthorized() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@gmail.com");
        loginRequest.setPassword("password");

        when(attendantService.authenticate(any(LoginRequest.class))).thenReturn(false);

        ResponseEntity<Object> response = cabinCrewController.attendantLogin(loginRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
