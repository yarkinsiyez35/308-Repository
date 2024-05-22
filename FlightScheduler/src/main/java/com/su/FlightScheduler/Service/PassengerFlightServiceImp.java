package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.DTO.SimplifiedPassengerDTO;
import com.su.FlightScheduler.DTO.forPassenger_FlightDTO;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PassengerFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class PassengerFlightServiceImp implements PassengerFlightService {


    private final PassengerServiceImp passengerService;
    private final PassengerFlightRepository passengerFlightRepository;
    //private final FlightServiceImp flightService;
    private final FlightRepository flightRepository;

    @Autowired
    public PassengerFlightServiceImp(PassengerServiceImp passengerService,
                                     PassengerFlightRepository passengerFlightRepository,
                                     /*FlightServiceImp flightService*/
                                     FlightRepository flightRepository) {
        this.passengerService = passengerService;
        this.passengerFlightRepository = passengerFlightRepository;
        //this.flightService = flightService;
        this.flightRepository = flightRepository;
    }

    @Override
    public PassengerFlight bookFlight(int passengerId, String flightNumber, String isParent, String seatNumber) {

        // Validate passenger
        PassengerEntity passenger = passengerService.findPassengerById(passengerId);
        // Validate flight
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow(
                () -> new RuntimeException("Flight with id: " + flightNumber + " does not exist!")
        );

        SimplifiedPassengerDTO passengerDTO = new SimplifiedPassengerDTO(passenger);
        forPassenger_FlightDTO flightDTO  = new forPassenger_FlightDTO(flight);

        // Create booking
        PassengerFlight book = new PassengerFlight(passenger, flight, isParent, seatNumber);

        //PassengerFlightDTO bookDTO = new PassengerFlightDTO(book);

        return passengerFlightRepository.save(book);
    }

    /*
    @Override
    public List<PassengerFlight> findBookedFlightsByFlightNumber(String flightNumber) {

        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow(
                () -> new RuntimeException("Flight with id: " + flightNumber + " does not exist!")
        );

        List<PassengerFlight> passengers = passengerFlightRepository.findPassengerFlightByFlight(flight);
        if (passengers.isEmpty()) {
            throw new NoSuchElementException("No passengers found for flight with ID: " + flightNumber); // no such element exception
        }
        return passengers;
    }
     */

    @Override
    public PassengerFlightDTO findBookingById(int id)
    {
        PassengerFlight passengerFlight = passengerFlightRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Booking with id: " + id + " does not exist!")
        );
        return new PassengerFlightDTO(passengerFlight);
    }

    @Override
    public PassengerFlightDTO cancelFlight(int id)
    {
        try
        {
            //find the passengerFlight to delete
            PassengerFlightDTO passengerFlight = findBookingById(id);
            //delete the passengerEntity
            passengerFlightRepository.deleteById(id);
            //return the deleted passengerFlight
            return passengerFlight;
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("Booking with id: " + id + " cannot be deleted!");
        }
    }

    public List<PassengerFlightDTO> findAllBookings(){

        List<PassengerFlightDTO> passengerFlights =
                passengerFlightRepository.findAll().stream()
                        .map(PassengerFlightDTO::new)
                        .collect(Collectors.toList());

        return passengerFlights;
    }


}
