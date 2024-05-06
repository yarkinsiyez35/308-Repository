package com.su.FlightScheduler.APIs.PassengerAPI;


import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping()
    public ResponseEntity<List<PassengerEntity>> getPassengers()
    {
        List<PassengerEntity> passengerEntityList = passengerService.findAllPassengers();
        return ResponseEntity.ok(passengerEntityList);
    }

    @GetMapping("/get/{passengerId}")
    public ResponseEntity<Object> getPassengerWithId(@PathVariable int passengerId)
    {
        Optional<PassengerEntity> passenger = passengerService.findPassengerById(passengerId);
        if (passenger.isPresent()) {
            return ResponseEntity.ok(passenger);
        }
        String message = "Passenger with ID: " + passengerId + " not found!";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> postPassengerWithId(@RequestBody PassengerEntity passengerEntity)
    {
        if (passengerService.findPassengerById(passengerEntity.getPassengerId()).isPresent())
        {
            String message = "Passenger with ID: " + passengerEntity.getPassengerId() + " already exists!";
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
        }

        PassengerEntity savedPassenger = passengerService.savePassenger(passengerEntity);

        return ResponseEntity.ok(savedPassenger);
    }

    @PutMapping("/update/{passengerId}")
    public ResponseEntity<PassengerEntity> updatePassengerWithId(@PathVariable int passengerId, @RequestBody PassengerEntity updatedPassenger) {
        Optional<PassengerEntity> passengerEntity = passengerService.findPassengerById(passengerId);
        if (passengerEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        updatedPassenger.setPassengerId(passengerId); // Ensure the ID is set correctly
        PassengerEntity updatedPassengerEntity = passengerService.updatePassenger(updatedPassenger);
        return ResponseEntity.ok(updatedPassengerEntity);
    }

    @DeleteMapping("/delete/{passengerId}")
    public ResponseEntity<Object> deletePassengerWithId(@PathVariable int passengerId)
    {
        Optional<PassengerEntity> passengerEntity = passengerService.findPassengerById(passengerId);
        if (passengerEntity.isPresent())
        {
            passengerService.deletePassengerById(passengerId);
            return ResponseEntity.ok(passengerEntity);
        }
        else
        {
            String message = "Passenger with ID: " + passengerId + " cannot be deleted!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> passengerLogin(@RequestBody LoginRequest loginRequest)
    {
        //not working
        boolean pilotExists = passengerService.authenticate(loginRequest);
        if (pilotExists)
        {
            //add stuff in future
            return ResponseEntity.ok(loginRequest);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
