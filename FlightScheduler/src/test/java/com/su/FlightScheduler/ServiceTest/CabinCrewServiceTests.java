package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.CabinLanguageRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepository;
import com.su.FlightScheduler.Service.AttendantService;
import com.su.FlightScheduler.Service.AttendantServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CabinCrewServiceTests {
    @InjectMocks
    private AttendantServiceImp attendantService;

    @Mock
    private CabinCrewRepository cabinCrewRepository;

    @Mock
    private CabinLanguageRepository cabinLanguageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void CabinCrewService_SaveAttendant_WithoutLanguages() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
// Mock the repository call
        when(cabinCrewRepository.save(any(CabinCrewEntity.class))).thenReturn(cabinCrewEntity);

        // When
        CabinCrewEntity savedCabinCrew = attendantService.saveCabin(cabinCrewEntity);

        // Then
        assertEquals(cabinCrewEntity.getAttendantId(), savedCabinCrew.getAttendantId());
        assertEquals(cabinCrewEntity.getEmail(), savedCabinCrew.getEmail());
        assertEquals(cabinCrewEntity.getPassword(), savedCabinCrew.getPassword());

        verify(cabinCrewRepository, times(1)).save(cabinCrewEntity);
    }



    @Test
    public void CabinCrewService_SaveAttendant_WithLanguages() {
        // Given
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        List<AttendantLanguageEntity> attendantLanguageEntityList = new ArrayList<>();
        attendantLanguageEntityList.add(new AttendantLanguageEntity(new AttendantLanguagePK(1, "English")));
        cabinCrewEntity.setLanguages(attendantLanguageEntityList);

        // Mock the repository calls
        when(cabinCrewRepository.save(any(CabinCrewEntity.class))).thenReturn(cabinCrewEntity);
        when(cabinLanguageRepository.saveAll(anyList())).thenReturn(attendantLanguageEntityList);

        // When
        CabinCrewEntity savedCabinCrewEntity = attendantService.saveCabin(cabinCrewEntity);

        // Then
        assertEquals(cabinCrewEntity.getAttendantId(), savedCabinCrewEntity.getAttendantId());
        assertEquals(cabinCrewEntity.getEmail(), savedCabinCrewEntity.getEmail());
        assertEquals(cabinCrewEntity.getLanguages().size(), savedCabinCrewEntity.getLanguages().size());
        verify(cabinCrewRepository, times(1)).save(any(CabinCrewEntity.class));
        verify(cabinLanguageRepository, times(1)).saveAll(anyList());
    }


    //TESTS FOR findattendantById()
    @Test
    public void PilotService_FindAttendantById_ExistingAttendant()
    {
        int id = 1;
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setAttendantId(id);

        when(cabinCrewRepository.findById(id)).thenReturn(Optional.of(cabinCrewEntity));
        cabinCrewEntity = attendantService.findAttendantById(id);
        assertNotNull(cabinCrewEntity);
        assertEquals(cabinCrewEntity.getAttendantId(), id);

        verify(cabinCrewRepository, times(1)).findById(id);
    }
      //Not Sure
    @Test
    public void AttendantService_FindAttendantById_NonExistingAttendant() {
        int id = 1;

        // Mock the repository to return an empty Optional when findById is called
        when(cabinCrewRepository.findById(id)).thenReturn(Optional.empty());

        // Assert that a specific exception is thrown when a non-existing attendant is requested
        Exception exception = assertThrows(RuntimeException.class, () -> {
            attendantService.findAttendantById(id);
        });

        // Check the exception message
        assertEquals("Attendant with id 1 does not exist!", exception.getMessage());

        // Verify that findById was called exactly once
        verify(cabinCrewRepository, times(1)).findById(id);
    }

    @Test
    public void AttendantService_AttendantExistsById_ExistingAttendant() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setAttendantId(1);

        // Mock the repository call to return true when existsById is called with ID 1
        when(cabinCrewRepository.existsById(1)).thenReturn(true);

        // Call the method under test
        boolean result = attendantService.cabinCrewExistsById(1);

        // Assert that the result is true as the attendant exists
        assertEquals(true, result);

        // Verify that existsById was called exactly once with the correct ID
        verify(cabinCrewRepository, times(1)).existsById(1);
    }

    @Test
    public void AttendantService_AttendantExistsById_NonExistingAttendant() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setAttendantId(1);

        // Mock the repository call to return true when existsById is called with ID 1
        when(cabinCrewRepository.existsById(1)).thenReturn(false);

        // Call the method under test
        boolean result = attendantService.cabinCrewExistsById(1);

        // Assert that the result is true as the attendant exists
        assertEquals(false, result);

        // Verify that existsById was called exactly once with the correct ID
        verify(cabinCrewRepository, times(1)).existsById(1);
    }


    //TEST FOR findAllCrew()
    @Test
    public void AttendantService_FindAllCrew()
    {
        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity();
        cabinCrewEntity1.setAttendantId(1);
        CabinCrewEntity cabinCrewEntity2 = new CabinCrewEntity();
        cabinCrewEntity2.setAttendantId(2);

        List<CabinCrewEntity> cabinCrewEntityList = new ArrayList<>();
        cabinCrewEntityList.add(cabinCrewEntity1);
        cabinCrewEntityList.add(cabinCrewEntity2);

        when(cabinCrewRepository.findAll()).thenReturn(cabinCrewEntityList);

        List<CabinCrewEntity> resultList = attendantService.findAllCabinCrew();

        assertEquals(resultList.size(), cabinCrewEntityList.size());

        verify(cabinCrewRepository, times(1)).findAll();
    }

    @Test
    public void AttendantService_UpdateCrew_WithLanguages() {
        int attendantId = 1;
        CabinCrewEntity attendant = new CabinCrewEntity();
        attendant.setAttendantId(attendantId);  // Using setId to set the identifier
        AttendantLanguageEntity language = new AttendantLanguageEntity(new AttendantLanguagePK(attendantId, "English"));
        attendant.setLanguages(List.of(language));

        // Mocking the necessary methods with correct repository and method names
        when(cabinCrewRepository.existsById(attendantId)).thenReturn(true);
        when(cabinLanguageRepository.findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(attendantId)).thenReturn(List.of(language));
        when(cabinLanguageRepository.saveAll(anyList())).thenReturn(List.of(language));
        when(cabinCrewRepository.save(any(CabinCrewEntity.class))).thenReturn(attendant);

        CabinCrewEntity updatedAttendant = attendantService.updateCabin(attendant);


        assertNotNull(updatedAttendant);
        assertEquals(attendantId, updatedAttendant.getAttendantId());


        verify(cabinCrewRepository, times(1)).existsById(attendantId);
        verify(cabinLanguageRepository, times(1)).findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(attendantId);
        verify(cabinLanguageRepository, times(1)).saveAll(anyList());
        verify(cabinCrewRepository, times(1)).save(any(CabinCrewEntity.class));
    }

    @Test
    public void AttendandtService_SaveAttendant_ExistingAttendant() {
        CabinCrewEntity attendant = new CabinCrewEntity();
        attendant.setAttendantId(1);

        when(cabinCrewRepository.existsById(1)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantService.saveCabin(attendant);
        });

        assertEquals("Cabin Crew with id 1 cannot be created!", exception.getMessage());

        verify(cabinCrewRepository, times(1)).existsById(1);
    }

}

