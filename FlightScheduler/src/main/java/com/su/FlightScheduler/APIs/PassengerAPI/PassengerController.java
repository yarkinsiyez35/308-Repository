package com.su.FlightScheduler.APIs.PassengerAPI;


import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

//TESTING: this controller should be tested, but not now
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



    @GetMapping("/{passengerId}")
    public ResponseEntity<Object> getPassengerWithId(@PathVariable int passengerId)
    {
        try {
            PassengerEntity passengerEntity = passengerService.findPassengerById(passengerId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_passenger_data_with_no_flight_from_passenger_entity(passengerEntity);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //this won't be used
    @PostMapping("/post")
    public ResponseEntity<Object> postPassenger(@RequestBody PassengerEntity passengerEntity)
    {
        try {
            PassengerEntity savedPassenger = passengerService.savePassenger(passengerEntity);
            return ResponseEntity.ok(savedPassenger);
        }
        catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{passengerId}")
    public ResponseEntity<Object> updatePassengerWithId(@PathVariable int passengerId, @RequestBody UserDataDTO userDataDTO) {
        try{

            PassengerEntity updatedPassenger = new PassengerEntity(userDataDTO);
            PassengerEntity passengerEntity = passengerService.findPassengerById(passengerId);
            updatedPassenger.setPassengerId(passengerId); // Ensure the ID is set correctly
            PassengerEntity updatedPassengerEntity = passengerService.updatePassenger(updatedPassenger);
            return ResponseEntity.ok(updatedPassengerEntity);
        }
        catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{passengerId}")
    public ResponseEntity<Object> deletePassengerWithId(@PathVariable int passengerId)
    {
        try{
            PassengerEntity passenger = passengerService.deletePassengerById(passengerId);
            return ResponseEntity.ok(passenger);
        }
        catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
