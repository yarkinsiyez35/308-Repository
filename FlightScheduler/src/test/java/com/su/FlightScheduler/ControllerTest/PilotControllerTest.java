package com.su.FlightScheduler.ControllerTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesAsStringDTO;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesDTO;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Service.PilotService;
import com.su.FlightScheduler.APIs.PilotAPI.PilotController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PilotControllerTest {

    @InjectMocks
    private PilotController pilotController;

    @Mock
    private PilotService pilotService;

    @Mock
    private UserDataDTOFactory userDataDTOFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    @Test
    public void testCreatePilotWithId_Success() {
        int pilotId = 1;
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();
        pilotWithLanguagesAsStringDTO.setPilotId(pilotId);
        PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO();
        PilotEntity pilotEntity = new PilotEntity();
        UserDataDTO userDataDTO = new UserDataDTO();

        when(pilotService.savePilot(any(PilotEntity.class))).thenReturn(pilotEntity);
        when(userDataDTOFactory.create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class))).thenReturn(userDataDTO);

        ResponseEntity<Object> response = pilotController.createPilotWithId(pilotId, pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).savePilot(any(PilotEntity.class));
        verify(userDataDTOFactory, times(1)).create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class));
    }

     */
    /*
    @Test
    public void testCreatePilotWithId_MethodNotAllowed() {
        int pilotId = 1;
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();
        pilotWithLanguagesAsStringDTO.setPilotId(pilotId);

        when(pilotService.savePilot(any(PilotEntity.class))).thenThrow(new RuntimeException("Pilot cannot be created"));

        ResponseEntity<Object> response = pilotController.createPilotWithId(pilotId, pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        verify(pilotService, times(1)).savePilot(any(PilotEntity.class));
    }

     */
    /*
    @Test
    public void testUpdatePilotWithId_Success() {
        int pilotId = 1;
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();
        pilotWithLanguagesAsStringDTO.setPilotId(pilotId);
        PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO();
        PilotEntity pilotEntity = new PilotEntity();
        PilotWithLanguagesDTO updatedPilotDTO = new PilotWithLanguagesDTO();
        PilotWithLanguagesAsStringDTO updatedPilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();

        when(pilotService.updatePilot(any(PilotEntity.class))).thenReturn(pilotEntity);
        when(userDataDTOFactory.create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class))).thenReturn(new UserDataDTO());

        ResponseEntity<Object> response = pilotController.updatePilotWithId(pilotId, pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).updatePilot(any(PilotEntity.class));
        verify(userDataDTOFactory, times(1)).create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class));
    }

     */
    /*

    @Test
    public void testUpdatePilotWithId_NotFound() {
        int pilotId = 1;
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();
        pilotWithLanguagesAsStringDTO.setPilotId(pilotId);

        when(pilotService.updatePilot(any(PilotEntity.class))).thenThrow(new RuntimeException("Pilot not found"));

        ResponseEntity<Object> response = pilotController.updatePilotWithId(pilotId, pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pilotService, times(1)).updatePilot(any(PilotEntity.class));
    }

     */

    /*
    @Test
    public void testGetPilotWithId_ExistingPilot() {
        int pilotId = 1;
        PilotEntity pilotEntity = new PilotEntity();
        UserDataDTO userDataDTO = new UserDataDTO();

        when(pilotService.findPilotById(pilotId)).thenReturn(pilotEntity);
        when(userDataDTOFactory.create_pilot_with_pilot_entity(any(PilotEntity.class))).thenReturn(userDataDTO);

        ResponseEntity<Object> response = pilotController.getPilotWithId(pilotId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).findPilotById(pilotId);
        verify(userDataDTOFactory, times(1)).create_pilot_with_pilot_entity(any(PilotEntity.class));
    }


     */
    @Test
    public void testGetPilotWithId_NonExistingPilot() {
        int pilotId = 1;

        when(pilotService.findPilotById(pilotId)).thenThrow(new RuntimeException("Pilot not found"));

        ResponseEntity<Object> response = pilotController.getPilotWithId(pilotId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pilotService, times(1)).findPilotById(pilotId);
    }
    /*

    @Test
    public void testCreatePilotWithoutId_Success() {
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();
        PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO();
        PilotEntity pilotEntity = new PilotEntity();
        PilotWithLanguagesDTO savedPilotDTO = new PilotWithLanguagesDTO();
        PilotWithLanguagesAsStringDTO savedPilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();

        when(pilotService.savePilotWithoutId(any(PilotEntity.class))).thenReturn(pilotEntity);
        when(userDataDTOFactory.create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class))).thenReturn(new UserDataDTO());

        ResponseEntity<Object> response = pilotController.createPilotWithoutId(pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).savePilotWithoutId(any(PilotEntity.class));
        verify(userDataDTOFactory, times(1)).create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class));
    }

     */

    /*
    @Test
    public void testCreatePilotWithoutId_MethodNotAllowed() {
        PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();

        when(pilotService.savePilotWithoutId(any(PilotEntity.class))).thenThrow(new RuntimeException("Pilot cannot be created"));

        ResponseEntity<Object> response = pilotController.createPilotWithoutId(pilotWithLanguagesAsStringDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        verify(pilotService, times(1)).savePilotWithoutId(any(PilotEntity.class));
    }

     */

    /*
    @Test
    public void testDeletePilotWithId_Success() {
        int pilotId = 1;
        PilotEntity pilotEntity = new PilotEntity();
        PilotWithLanguagesDTO deletedPilotDTO = new PilotWithLanguagesDTO();
        PilotWithLanguagesAsStringDTO deletedPilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO();

        when(pilotService.deletePilotById(pilotId)).thenReturn(pilotEntity);
        when(userDataDTOFactory.create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class))).thenReturn(new UserDataDTO());

        ResponseEntity<Object> response = pilotController.deletePilotWithId(pilotId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).deletePilotById(pilotId);
        verify(userDataDTOFactory, times(1)).create_pilot_data_with_pilotWithLanguagesDTO(any(PilotWithLanguagesDTO.class));
    }

     */

    /*
    @Test
    public void testDeletePilotWithId_NotFound() {
        int pilotId = 1;

        when(pilotService.deletePilotById(pilotId)).thenThrow(new RuntimeException("Pilot not found"));

        ResponseEntity<Object> response = pilotController.deletePilotWithId(pilotId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pilotService, times(1)).deletePilotById(pilotId);
    }

     */

    /*
    @Test
    public void testGetPilots() {
        List<PilotEntity> pilotEntityList = new ArrayList<>();
        List<PilotWithLanguagesAsStringDTO> pilotWithLanguagesAsStringDTOList = new ArrayList<>();

        when(pilotService.findAllPilots()).thenReturn(pilotEntityList);

        ResponseEntity<List<PilotWithLanguagesAsStringDTO>> response = pilotController.getPilots();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pilotService, times(1)).findAllPilots();
    }

     */
}
