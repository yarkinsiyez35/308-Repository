package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    }

    @Override
    public Optional<PassengerEntity> findPassengerById(int id) {
        return passengerRepository.findById(id);
    }

    @Override
    public List<PassengerEntity> findAllPassengers() {
        return passengerRepository.findAll();
    }

    @Override
    public PassengerEntity updatePassenger(PassengerEntity passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public void deletePassengerById(int id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(LoginRequest loginRequest) {
        Optional<PassengerEntity> passengerEntity = passengerRepository.findPassengerEntityByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return passengerEntity.isPresent();
    }
}
