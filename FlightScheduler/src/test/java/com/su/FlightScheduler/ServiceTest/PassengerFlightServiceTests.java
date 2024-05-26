package com.su.FlightScheduler.ServiceTest;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PassengerFlightRepository;
import com.su.FlightScheduler.Service.FlightService;
import com.su.FlightScheduler.Service.PassengerFlightServiceImp;

import com.su.FlightScheduler.Service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PassengerFlightServiceTests {

    @InjectMocks
    private PassengerFlightServiceImp bookingService;

    @Mock
    private FlightService flightService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PassengerFlightRepository bookingRepo;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    //TESTS FOR FindBookingById()
    @Test
    public void FindBookingById_Existing()
    {
        int id = 1;
        PassengerFlight passengerFlight = new PassengerFlight();
        passengerFlight.setBookingId(id);
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber("1");
        passengerFlight.setFlight(flightEntity);

        when(bookingRepo.findById(id)).thenReturn(Optional.of(passengerFlight));

        PassengerFlight found = bookingService.findBookingById(id);

        assertNotNull(found);
        assertEquals(id, found.getBookingId());

        verify(bookingRepo, times(1)).findById(id);
    }

    @Test
    public void FindBookingById_NonExisting()
    {
        int id = 1;

        when(bookingRepo.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.findBookingById(id);
        });

        assertEquals("Booking with id: " + id + " does not exist!", exception.getMessage());

        verify(bookingRepo, times(1)).findById(id);
    }

    //TEST FOR findAllBookings()
    @Test
    public void FindAllBookings()
    {
        PassengerFlight booking1 = new PassengerFlight();
        booking1.setBookingId(1);
        PassengerFlight booking2 = new PassengerFlight();
        booking2.setBookingId(2);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber("1");
        booking1.setFlight(flightEntity);
        booking2.setFlight(flightEntity);

        List<PassengerFlight> passengerFlightList = new ArrayList<>();
        passengerFlightList.add(booking1);
        passengerFlightList.add(booking2);

        when(bookingRepo.findAll()).thenReturn(passengerFlightList);

        List<PassengerFlightDTO> resultList = bookingService.findAllBookings();

        assertEquals(resultList.size(), passengerFlightList.size());

        verify(bookingRepo, times(1)).findAll();
    }
}