package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.DTO.SimplifiedPassengerDTO;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class PassengerServiceImp implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerServiceImp(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }


    @Override
    public PassengerEntity savePassenger(PassengerEntity passenger) {
        PassengerEntity passengerEntity = new PassengerEntity(passenger);
        return passengerRepository.save(passengerEntity);
    } // might need update
        // if necessary info not got from user
        // passenger should not be created

    @Override
    public PassengerEntity findPassengerById(int id)
    {
        //get passengerEntity from the repository
        PassengerEntity passenger = passengerRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Passenger with id: " + id + " does not exist!")
        );
        return passenger;
    }

    @Override
    public List<PassengerEntity> findAllPassengers()
    {
        return passengerRepository.findAll();
    }

    @Override
    public boolean passengerExistsById(int id) {
        return passengerRepository.existsById(id);
    }

    @Override
    public PassengerEntity updatePassenger(PassengerEntity passenger)
    {
        if (!passengerExistsById(passenger.getPassengerId()))   //a nonexistent passenger cannot be updated
        {
            throw new RuntimeException("Passenger with id: " + passenger.getPassengerId() + " cannot be updated!");
        }

        PassengerEntity updatedPassenger = passengerRepository.save(passenger);
        //return updated entity
        return updatedPassenger;
    }

    @Override
    public PassengerEntity deletePassengerById(int id)
    {
        try //findPassengerById() throws exception
        {
            //find the passengerEntity to delete
            PassengerEntity passengerEntity = findPassengerById(id);
            //delete the passengerEntity
            passengerRepository.deleteById(id);
            //return the deleted passengerEntity
            return passengerEntity;
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("Passenger with id: " + id + " cannot be deleted!");
        }
    }

    @Override
    public boolean authenticate(LoginRequest loginRequest) {
        Optional<PassengerEntity> passengerEntity = passengerRepository.findPassengerEntityByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return passengerEntity.isPresent();
    }

    @Override
    public List<PassengerFlightDTO> findBookedFlightsByPassengerId(int passengerId) {

        PassengerEntity passenger = findPassengerById(passengerId); //runtime exception

        //List<PassengerFlight> bookedFlights = passengerFlightRepository.findPassengerFlightByPassenger(passenger);
        SimplifiedPassengerDTO passengerDTO = new SimplifiedPassengerDTO(passenger);
        List<PassengerFlightDTO> bookedFlights = passengerDTO.getPassengerFlights();

        if (bookedFlights.isEmpty()) {
            throw new NoSuchElementException("No flights found for passenger with ID: " + passengerId); // no such element exception
        }
        return bookedFlights;
    }
}
