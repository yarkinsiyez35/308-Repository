package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.DTO.SimplifiedPassengerDTO;
import com.su.FlightScheduler.DTO.forPassenger_FlightDTO;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PassengerFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import java.util.ArrayList;

//TESTING: this service should be tested
@Service
@Transactional
public class PassengerFlightServiceImp implements PassengerFlightService {


    private final PassengerServiceImp passengerService;
    private final PassengerFlightRepository passengerFlightRepository;
    //private final FlightServiceImp flightService;
    private final FlightRepository flightRepository;

    private final FlightService flightService;

    @Autowired
    public PassengerFlightServiceImp(PassengerServiceImp passengerService,
                                     PassengerFlightRepository passengerFlightRepository,
                                     FlightService flightService,
                                     FlightRepository flightRepository) {
        this.passengerService = passengerService;
        this.passengerFlightRepository = passengerFlightRepository;
        this.flightService = flightService;
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
    public PassengerFlight findBookingById(int id) {
        PassengerFlight passengerFlight = passengerFlightRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Booking with id: " + id + " does not exist!")
        );
        return passengerFlight;
    }

    @Override
    public PassengerFlight cancelFlight(int id) {
        try {
            //find the passengerFlight to delete
            PassengerFlight passengerFlight = findBookingById(id);
            //delete the passengerEntity
            passengerFlightRepository.deleteById(id);
            //return the deleted passengerFlight
            return passengerFlight;
        } catch (RuntimeException e) {
            throw new RuntimeException("Booking with id: " + id + " cannot be deleted!");
        }
    }

    public List<PassengerFlightDTO> findAllBookings() {

        List<PassengerFlightDTO> passengerFlights =
                passengerFlightRepository.findAll().stream()
                        .map(PassengerFlightDTO::new)
                        .collect(Collectors.toList());

        return passengerFlights;
    }

    @Override
    public UserDataDTO findBookedFlightsByPassengerId(int passengerId) {

        PassengerEntity passenger = passengerService.findPassengerById(passengerId); //runtime exception

        //List<PassengerFlight> bookedFlights = passengerFlightRepository.findPassengerFlightByPassenger(passenger);
        //SimplifiedPassengerDTO passengerDTO = new SimplifiedPassengerDTO(passenger);
        List<PassengerFlight> bookedFlights = passenger.getPassengerFlights();

        UserDataDTO userDataDTO = UserDataDTOFactory.create_passenger_data_with_flight_list(bookedFlights, passenger);

        return userDataDTO;
    }

    @Override
    public PassengerFlight bookFlightAuto(int passengerId, String flightNumber, String isParent, Boolean isEconomy) {
        PassengerEntity passenger = passengerService.findPassengerById(passengerId);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow(
                () -> new RuntimeException("Flight with id: " + flightNumber + " does not exist!")
        );


        List<SeatingDTO> seating = flightService.findBookedFlightsByFlightNumber(flightNumber);
        List<SeatingTypeDTO> seatPlan = flightService.decodeSeatingPlan(flightNumber);
        int intervalStart = 0;
        int intervalEnd = 0;
        if (isEconomy) {
            intervalStart = seatPlan.get(1).getStartRow();
            intervalEnd = seatPlan.get(1).getEndRow();
        } else {
            intervalStart = seatPlan.get(0).getStartRow();
            intervalEnd = seatPlan.get(0).getEndRow();
        }

        String columns = seatPlan.get(0).getColumns();
        List<String> availableSeatsToChoose = new ArrayList<>();
        char lastChar = columns.charAt(columns.length() - 1);
        int A_ASCII = 65;
        int lastChar_ASCII = (int) lastChar;
        for (int i = A_ASCII; i <= lastChar_ASCII; i++) {
            availableSeatsToChoose.add(String.valueOf((char) i));
        }

        List<String> availableSeats = flightService.getAvailableSeats(flightNumber);
        if(availableSeats.size() == 0) {
            throw new RuntimeException("No available seats for flight with ID: " + flightNumber);
        }
        String seatNumber = "";
        do {
            seatNumber = randomSeat(seating, seatPlan, intervalStart, intervalEnd, columns);
        } while(!availableSeats.contains(seatNumber));

        PassengerFlight book = new PassengerFlight(passenger, flight, isParent, seatNumber);
        return passengerFlightRepository.save(book);
    }

    private String randomSeat(List<SeatingDTO> seating, List<SeatingTypeDTO> seatPlan, int intervalStart, int intervalEnd, String columns) {
        String seatNumber = "";
        List<String> availableSeatsToChoose = new ArrayList<>();
        char lastChar = columns.charAt(columns.length() - 1);
        int A_ASCII = 65;
        int lastChar_ASCII = (int) lastChar;
        for (int i = A_ASCII; i <= lastChar_ASCII; i++) {
            availableSeatsToChoose.add(String.valueOf((char) i));
        }

        Random rand = new Random();
        int row = intervalStart + rand.nextInt(intervalEnd - intervalStart + 1);
        int columnIndex = rand.nextInt(availableSeatsToChoose.size());
        String column = availableSeatsToChoose.get(columnIndex);
        seatNumber = row + column;
        return seatNumber;
    }

}
