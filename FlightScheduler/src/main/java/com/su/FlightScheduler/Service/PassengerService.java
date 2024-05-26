package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.Entity.PassengerEntity;

import java.util.List;
import java.util.Optional;

public interface PassengerService {

    public PassengerEntity savePassenger(PassengerEntity passenger);
    public PassengerEntity findPassengerById(int id);
    public boolean passengerExistsById(int id);
    public List<PassengerEntity> findAllPassengers();
    public PassengerEntity updatePassenger(PassengerEntity passenger);
    public PassengerEntity deletePassengerById(int id);
    public boolean authenticate(LoginRequest loginRequest);
    //public List<PassengerFlightDTO> findBookedFlightsByPassengerId(int passengerId);
}
