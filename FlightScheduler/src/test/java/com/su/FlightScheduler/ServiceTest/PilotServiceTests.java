package com.su.FlightScheduler.ServiceTest;


import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotLanguageRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Service.PilotServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class PilotServiceTests {

    @InjectMocks
    private PilotServiceImp pilotService;

    @Mock
    private PilotRepository pilotRepository;
    @Mock
    private PilotLanguageRepository pilotLanguageRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }


    //TESTS FOR savePilot()
    @Test
    public void PilotService_SavePilot_WithoutLanguages()
    {
        //given
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");

        //mock the calls
        Mockito.when(pilotRepository.save(pilotEntity)).thenReturn(pilotEntity);

        //when
        PilotEntity savedPilot = pilotService.savePilot(pilotEntity);

        //then
        assertEquals(savedPilot.getPilotId(), pilotEntity.getPilotId());
        assertEquals(savedPilot.getEmail(), pilotEntity.getEmail());
        assertEquals(savedPilot.getPassword(), pilotEntity.getPassword());

        verify(pilotRepository, times(1)).save(pilotEntity);
    }

    @Test
    public void PilotService_SavePilot_WithLanguages()
    {
        //given
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        List<PilotLanguageEntity> pilotLanguageEntityList = new ArrayList<>();
        pilotLanguageEntityList.add(new PilotLanguageEntity(new PilotLanguagePK(1, "English")));
        pilotEntity.setLanguages(pilotLanguageEntityList);

        //mock the calls
        Mockito.when(pilotRepository.save(any(PilotEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(pilotLanguageRepository.saveAll(anyList())).thenReturn(pilotLanguageEntityList);

        //when
        PilotEntity savedPilot = pilotService.savePilot(pilotEntity);

        assertEquals(savedPilot.getPilotId(), pilotEntity.getPilotId());
        assertEquals(savedPilot.getEmail(), pilotEntity.getEmail());
        assertEquals(savedPilot.getPassword(), pilotEntity.getPassword());
        assertEquals(savedPilot.getLanguages().size(), pilotEntity.getLanguages().size());

        verify(pilotRepository, times(1)).save(any(PilotEntity.class));
        verify(pilotLanguageRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void PilotService_SavePilot_ExistingPilot() {
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(1);

        when(pilotRepository.existsById(1)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotService.savePilot(pilot);
        });

        assertEquals("Pilot with id 1 cannot be created!", exception.getMessage());

        verify(pilotRepository, times(1)).existsById(1);
    }


    //TESTS FOR findPilotById()
    @Test
    public void PilotService_FindPilotById_ExistingPilot()
    {
        int id = 1;
        PilotEntity pilotEntity = new PilotEntity();
        pilotEntity.setPilotId(id);

        when(pilotRepository.findById(id)).thenReturn(Optional.of(pilotEntity));

        PilotEntity foundPilot = pilotService.findPilotById(id);

        assertNotNull(foundPilot);
        assertEquals(id, foundPilot.getPilotId());

        verify(pilotRepository, times(1)).findById(id);
    }

    @Test
    public void PilotService_FindPilotById_NonExistingPilot()
    {
        int id = 1;

        when(pilotRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotService.findPilotById(id);
        });

        assertEquals("Pilot with id: " + id + " does not exist!", exception.getMessage());

        verify(pilotRepository, times(1)).findById(id);
    }

    //TESTS for pilotExistsById()
    @Test
    public void PilotService_PilotExistsById_ExistingPilot()
    {
        PilotEntity pilotEntity = new PilotEntity();
        pilotEntity.setPilotId(1);

        when(pilotRepository.existsById(1)).thenReturn(true);

        boolean result = pilotService.pilotExistsById(1);

        assertEquals(result, true);

        verify(pilotRepository, times(1)).existsById(1);
    }

    @Test
    public void PilotService_PilotExistsById_NonExistingPilot()
    {
        PilotEntity pilotEntity = new PilotEntity();
        pilotEntity.setPilotId(1);

        when(pilotRepository.existsById(1)).thenReturn(false);

        boolean result = pilotService.pilotExistsById(1);

        assertEquals(result, false);

        verify(pilotRepository, times(1)).existsById(1);
    }

    //TEST FOR findAllPilots()
    @Test
    public void PilotService_FindAllPilots()
    {
        PilotEntity pilotEntity1 = new PilotEntity();
        pilotEntity1.setPilotId(1);
        PilotEntity pilotEntity2 = new PilotEntity();
        pilotEntity2.setPilotId(2);

        List<PilotEntity> pilotEntityList = new ArrayList<>();
        pilotEntityList.add(pilotEntity1);
        pilotEntityList.add(pilotEntity2);

        when(pilotRepository.findAll()).thenReturn(pilotEntityList);

        List<PilotEntity> resultList = pilotService.findAllPilots();

        assertEquals(resultList.size(), pilotEntityList.size());

        verify(pilotRepository, times(1)).findAll();
    }



    //TESTS FOR updatePilot()
    @Test
    public void PilotService_UpdatePilot_WithLanguages()
    {
        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);
        PilotLanguageEntity language = new PilotLanguageEntity(new PilotLanguagePK(pilotId, "English"));
        pilot.setLanguages(List.of(language));

        when(pilotRepository.existsById(pilotId)).thenReturn(true);
        when(pilotLanguageRepository.findPilotLanguageEntitiesByPilotLanguagePK_PilotId(pilotId)).thenReturn(List.of(language));
        when(pilotLanguageRepository.saveAll(anyList())).thenReturn(List.of(language));
        when(pilotRepository.save(any(PilotEntity.class))).thenReturn(pilot);

        PilotEntity updatedPilot = pilotService.updatePilot(pilot);

        assertNotNull(updatedPilot);
        assertEquals(pilotId, updatedPilot.getPilotId());

        verify(pilotRepository, times(1)).existsById(pilotId);
        verify(pilotLanguageRepository, times(1)).findPilotLanguageEntitiesByPilotLanguagePK_PilotId(pilotId);
        verify(pilotLanguageRepository, times(1)).deleteAll(anyList());
        verify(pilotRepository, times(1)).save(any(PilotEntity.class));
    }

    @Test
    public void PilotService_UpdatePilot_WithoutLanguages()
    {
        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);

        when(pilotRepository.existsById(pilotId)).thenReturn(true);
        when(pilotRepository.save(any(PilotEntity.class))).thenReturn(pilot);
        PilotEntity updatedPilot = pilotService.updatePilot(pilot);

        assertNotNull(updatedPilot);
        assertEquals(pilotId, updatedPilot.getPilotId());

        verify(pilotRepository, times(1)).existsById(pilotId);
        verify(pilotRepository, times(1)).save(any(PilotEntity.class));
    }

    @Test
    public void PilotService_UpdatePilot_NotExisting()
    {
        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);

        when(pilotRepository.existsById(pilotId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotService.updatePilot(pilot);
        });

        assertEquals("Pilot with id: " + pilotId + " cannot be updated!", exception.getMessage());

        verify(pilotRepository, times(1)).existsById(pilotId);
    }

    @Test
    public void PilotService_DeletePilot_ExistingPilot()
    {
        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);

        when(pilotRepository.findById(pilotId)).thenReturn(Optional.of(pilot));

        PilotEntity deletedPilot = pilotService.deletePilotById(1);

        assertEquals(pilotId, deletedPilot.getPilotId());

        verify(pilotRepository, times(1)).findById(pilotId);
        verify(pilotRepository, times(1)).deleteById(pilotId);
    }


    @Test
    public void PilotService_DeletePilot_NonExistingPilot()
    {
        int pilotId = 1;
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(pilotId);

        when(pilotRepository.findById(pilotId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pilotService.deletePilotById(pilotId);
        });

        assertEquals("Pilot with id: " + pilotId + " cannot be deleted!", exception.getMessage());

        verify(pilotRepository, times(1)).findById(pilotId);
    }
}
