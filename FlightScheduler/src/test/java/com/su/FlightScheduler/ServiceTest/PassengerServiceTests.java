package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Repository.PassengerRepository;
import com.su.FlightScheduler.Service.PassengerServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PassengerServiceTests {

    @InjectMocks
    private PassengerServiceImp passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }


    //TESTS FOR savePassenger()
    @Test
    public void SavePassenger()
    {
        //given
        PassengerEntity passengerEntity = new PassengerEntity(
                "email@gmail.com",
                "password",
                "ismet",
                "ayvaz",
                22,
                "male",
                "Turkish"
        );

        //mock the calls
        Mockito.when(passengerRepository.save(passengerEntity)).thenReturn(passengerEntity);

        //when
        PassengerEntity savedPassenger = passengerService.savePassenger(passengerEntity);

        //then
        assertEquals(savedPassenger.getPassengerId(), passengerEntity.getPassengerId());
        assertEquals(savedPassenger.getEmail(), passengerEntity.getEmail());
        assertEquals(savedPassenger.getPassword(), passengerEntity.getPassword());

        verify(passengerRepository, times(1)).save(passengerEntity);
    }

    //TESTS FOR findPassengerById()
    @Test
    public void FindPassengerById_ExistingPassenger()
    {
        int id = 1;
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(id);

        when(passengerRepository.findById(id)).thenReturn(Optional.of(passengerEntity));

        PassengerEntity foundPassenger = passengerService.findPassengerById(id);

        assertNotNull(foundPassenger);
        assertEquals(id, foundPassenger.getPassengerId());

        verify(passengerRepository, times(1)).findById(id);
    }

    @Test
    public void FindPassengerById_NonExistingPassenger()
    {
        int id = 1;

        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passengerService.findPassengerById(id);
        });

        assertEquals("Passenger with id: " + id + " does not exist!", exception.getMessage());

        verify(passengerRepository, times(1)).findById(id);
    }

    //TESTS for passengerExistsById()
    @Test
    public void PassengerExistsById_ExistingPassenger()
    {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);

        when(passengerRepository.existsById(1)).thenReturn(true);

        boolean result = passengerService.passengerExistsById(1);

        assertEquals(result, true);

        verify(passengerRepository, times(1)).existsById(1);
    }

    @Test
    public void PassengerExistsById_NonExistingPassenger()
    {
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(1);

        when(passengerRepository.existsById(1)).thenReturn(false);

        boolean result = passengerService.passengerExistsById(1);

        assertEquals(result, false);

        verify(passengerRepository, times(1)).existsById(1);
    }

    //TEST FOR findAllPassengers()
    @Test
    public void FindAllPassengers()
    {
        PassengerEntity passengerEntity1 = new PassengerEntity();
        passengerEntity1.setPassengerId(1);
        PassengerEntity passengerEntity2 = new PassengerEntity();
        passengerEntity2.setPassengerId(2);

        List<PassengerEntity> passengerEntityList = new ArrayList<>();
        passengerEntityList.add(passengerEntity1);
        passengerEntityList.add(passengerEntity2);

        when(passengerRepository.findAll()).thenReturn(passengerEntityList);

        List<PassengerEntity> resultList = passengerService.findAllPassengers();

        assertEquals(resultList.size(), passengerEntityList.size());

        verify(passengerRepository, times(1)).findAll();
    }



    //TESTS FOR updatePassenger()
    @Test
    public void UpdatePassenger_Existing()
    {
        int id = 1;
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setPassengerId(id);

        when(passengerRepository.existsById(id)).thenReturn(true);
        when(passengerRepository.save(any(PassengerEntity.class))).thenReturn(passengerEntity);

        PassengerEntity passenger = passengerService.updatePassenger(passengerEntity);

        assertNotNull(passenger);
        assertEquals(id, passenger.getPassengerId());

        verify(passengerRepository, times(1)).existsById(id);
        verify(passengerRepository, times(1)).save(any(PassengerEntity.class));
    }

    @Test
    public void UpdatePassenger_NonExisting()
    {
        int passengerId = 1;
        PassengerEntity passenger = new PassengerEntity();
        passenger.setPassengerId(passengerId);

        when(passengerRepository.existsById(passengerId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passengerService.updatePassenger(passenger);
        });

        assertEquals("Passenger with id: " + passengerId + " cannot be updated!", exception.getMessage());

        verify(passengerRepository, times(1)).existsById(passengerId);
    }

    @Test
    public void DeletePassenger_Existing()
    {
        int passengerId = 1;
        PassengerEntity passenger = new PassengerEntity();
        passenger.setPassengerId(passengerId);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        PassengerEntity deletedPassenger = passengerService.deletePassengerById(1);

        assertEquals(passengerId, deletedPassenger.getPassengerId());

        verify(passengerRepository, times(1)).findById(passengerId);
        verify(passengerRepository, times(1)).deleteById(passengerId);
    }

    @Test
    public void DeletePassenger_NonExisting()
    {
        int passengerId = 1;
        PassengerEntity passenger = new PassengerEntity();
        passenger.setPassengerId(passengerId);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passengerService.deletePassengerById(passengerId);
        });

        assertEquals("Passenger with id: " + passengerId + " cannot be deleted!", exception.getMessage());

        verify(passengerRepository, times(1)).findById(passengerId);
    }
}
