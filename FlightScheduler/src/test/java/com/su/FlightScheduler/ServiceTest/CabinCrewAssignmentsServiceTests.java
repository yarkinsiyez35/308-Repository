package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsPK;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.PlaneEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Service.AttendantAssignmentServiceImp;
import com.su.FlightScheduler.Util.SeatIncrementer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CabinCrewAssignmentsServiceTests {

    @InjectMocks
    private AttendantAssignmentServiceImp attendantAssignmentService;

    @Mock
    private CabinAssignmentRepository cabinAssignmentRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private CabinCrewRepository cabinCrewRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TESTS FOR getFlightsOfAttendant()
    /*
    @Test
    public void testGetFlightsOfAttendant_ExistingAttendant() {
        int attendantId = 1;
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setAttendantId(attendantId);
        cabinCrewEntity.setEmail("email@example.com");

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = new ArrayList<>();
        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity();
        cabinCrewAssignmentsEntity.setCabinCrew(cabinCrewEntity);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setSourceAirport(new AirportEntity());
        cabinCrewAssignmentsEntity.setFlight(flightEntity);

        cabinCrewAssignmentsEntityList.add(cabinCrewAssignmentsEntity);

        when(cabinCrewRepository.findById(attendantId)).thenReturn(Optional.of(cabinCrewEntity));
        when(cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(attendantId)).thenReturn(cabinCrewAssignmentsEntityList);

        UserDataDTO result = UserDataDTOFactory.create_cabin_crew_data_with_flight_list(cabinCrewAssignmentsEntityList, cabinCrewEntity);

        assertNotNull(result);
        verify(cabinCrewRepository, times(1)).findById(attendantId);
        verify(cabinAssignmentRepository, times(1)).findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(attendantId);
    }

     */


    @Test
    public void testGetFlightsOfAttendant_NonExistingAttendant() {
        int attendantId = 1;

        when(cabinCrewRepository.findById(attendantId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantAssignmentService.getFlightsOfAttendant(attendantId);
        });

        assertEquals("Attendant with id: " + attendantId + " does not exist!", exception.getMessage());
        verify(cabinCrewRepository, times(1)).findById(attendantId);
    }

    // TESTS FOR assignAttendantToFlight()
    @Test
    public void testAssignAttendantToFlight_FlightDoesNotExist() {
        String flightNumber = "FN123";
        int attendantId = 1;

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantAssignmentService.assignAttendantToFlight(flightNumber, attendantId);
        });

        assertEquals("Flight with id: " + flightNumber + "does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }

    @Test
    public void testAssignAttendantToFlight_AttendantDoesNotExist() {
        String flightNumber = "FN123";
        int attendantId = 1;

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(cabinCrewRepository.existsById(attendantId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantAssignmentService.assignAttendantToFlight(flightNumber, attendantId);
        });

        assertEquals("Cabin crew member with id: " + attendantId + "does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(cabinCrewRepository, times(1)).existsById(attendantId);
    }

    /*
    @Test
    public void testAssignAttendantToFlight_Success() {
        String flightNumber = "FN123";
        int attendantId = 1;

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber(flightNumber);
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setAttendantId(attendantId);

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = new ArrayList<>();
        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity();
        cabinCrewAssignmentsEntityList.add(cabinCrewAssignmentsEntity);

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(cabinCrewRepository.existsById(attendantId)).thenReturn(true);
        when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(flightEntity));
        when(cabinCrewRepository.findById(attendantId)).thenReturn(Optional.of(cabinCrewEntity));
        when(cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber)).thenReturn(cabinCrewAssignmentsEntityList);
        when(SeatIncrementer.findLastCabinCrewSeat(anyList())).thenReturn("0A");
        when(SeatIncrementer.incrementSeat("0A")).thenReturn("0B");
        when(cabinAssignmentRepository.save(any(CabinCrewAssignmentsEntity.class))).thenReturn(new CabinCrewAssignmentsEntity());

        UserDataDTO result = attendantAssignmentService.assignAttendantToFlight(flightNumber, attendantId);

        assertNotNull(result);
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(cabinCrewRepository, times(1)).existsById(attendantId);
        verify(flightRepository, times(1)).findById(flightNumber);
        verify(cabinCrewRepository, times(1)).findById(attendantId);
        verify(cabinAssignmentRepository, times(1)).findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
        verify(cabinAssignmentRepository, times(1)).save(any(CabinCrewAssignmentsEntity.class));
    }

     */


    // TESTS FOR getAttendantsOfFlight()
    /*
    @Test
    public void testGetAttendantsOfFlight_FlightExists() {
        String flightNumber = "FN123";
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity();
        cabinCrewEntity.setEmail("email@example.com");
        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity();
        cabinCrewAssignmentsEntity.setCabinCrew(cabinCrewEntity);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber(flightNumber);
        flightEntity.setSourceAirport(new AirportEntity());

        cabinCrewAssignmentsEntity.setFlight(flightEntity);

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = new ArrayList<>();
        cabinCrewAssignmentsEntityList.add(cabinCrewAssignmentsEntity);

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber)).thenReturn(cabinCrewAssignmentsEntityList);

        List<UserDataDTO> result = new ArrayList<>();
        for (CabinCrewAssignmentsEntity entity : cabinCrewAssignmentsEntityList) {
            result.add(UserDataDTOFactory.create_cabin_crew_data_with_assignment(entity));
        }

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(cabinAssignmentRepository, times(1)).findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
    }


     */

    @Test
    public void testGetAttendantsOfFlight_FlightDoesNotExist() {
        String flightNumber = "FN123";

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantAssignmentService.getAttendantsOfFlight(flightNumber);
        });

        assertEquals("Cabin Crew member with id: " + flightNumber + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }

    // TESTS FOR getAvailableAttendantsForFlight()
    @Test
    public void testGetAvailableAttendantsForFlight_FlightDoesNotExist() {
        String flightNumber = "FN123";

        when(flightRepository.existsById(flightNumber)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            attendantAssignmentService.getAvailableAttendantsForFlight(flightNumber);
        });

        assertEquals("Flight with id: " + flightNumber + " does not exist!", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightNumber);
    }


    /*
    @Test
    public void testGetAvailableAttendantsForFlight_Success() {
        String flightNumber = "FN123";
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber(flightNumber);
        flightEntity.setPlane(new PlaneEntity());
        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = new ArrayList<>();
        List<CabinCrewEntity> availableAttendantList = new ArrayList<>();

        when(flightRepository.existsById(flightNumber)).thenReturn(true);
        when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(flightEntity));
        when(cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber)).thenReturn(cabinCrewAssignmentsEntityList);
        when(cabinCrewRepository.findCabinCrewEntityBySeniority(anyString())).thenReturn(availableAttendantList);

        List<UserDataDTO> result = UserDataDTOFactory.create_available_attendant_list(availableAttendantList);

        assertNotNull(result);
        verify(flightRepository, times(1)).existsById(flightNumber);
        verify(flightRepository, times(1)).findById(flightNumber);
        verify(cabinAssignmentRepository, times(1)).findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
        verify(cabinCrewRepository, times(1)).findCabinCrewEntityBySeniority(anyString());
    }

     */

}
