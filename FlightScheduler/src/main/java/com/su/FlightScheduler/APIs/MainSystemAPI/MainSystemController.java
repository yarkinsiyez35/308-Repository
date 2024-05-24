package com.su.FlightScheduler.APIs.MainSystemAPI;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Service.AttendantAssignmentService;
import com.su.FlightScheduler.Service.PassengerFlightService;
import com.su.FlightScheduler.Service.PilotFlightAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/main")
public class MainSystemController {
    private final PilotFlightAssignmentService pilotFlightAssignmentService;
    private final AttendantAssignmentService attendantAssignmentService;
    private final PassengerFlightService passengerFlightService;

    @Autowired
    public MainSystemController(PilotFlightAssignmentService pilotFlightAssignmentService, AttendantAssignmentService attendantAssignmentService, PassengerFlightService passengerFlightService) {
        this.pilotFlightAssignmentService = pilotFlightAssignmentService;
        this.attendantAssignmentService = attendantAssignmentService;
        this.passengerFlightService = passengerFlightService;
    }

    //functions for pilot and flight assignments
    @GetMapping("/pilot/{pilotId}/getFlights")
    public ResponseEntity<Object> getFlightsOfPilot(@PathVariable int pilotId)
    {  //this function returns the assignments of a given pilot
        try
        {
            UserDataDTO results = pilotFlightAssignmentService.getFlightsOfPilot(pilotId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/flight/{flightId}/getAvailablePilots")
    public ResponseEntity<Object> getAvailablePilotsForFlight(@PathVariable String flightId)
    {   //this function returns the available pilots for a given flight
        try
        {
            List<UserDataDTO> results = pilotFlightAssignmentService.getAvailablePilotsForFlight(flightId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getPilots")
    public ResponseEntity<Object> getPilotsOfFlight(@PathVariable String flightId)
    {   //this function returns the active pilots for a given flight
        try
        {
            List<UserDataDTO> userDataDTOList = pilotFlightAssignmentService.getPilotsOfFlight(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/pilot/{pilotId}/assignToFlight/{flightId}")
    public ResponseEntity<Object> assignPilotToFlight(@PathVariable int pilotId, @PathVariable String flightId)
    {   //this function assigns the pilot to a given flight and returns the updated pilot information
        try
        {
            UserDataDTO userDataDTO = pilotFlightAssignmentService.assignPilotToFlight(flightId, pilotId);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @DeleteMapping("/pilot/{pilotId}/removeFromFlight/{flightId}")
    public ResponseEntity<Object> removePilotFromFlight(@PathVariable int pilotId, @PathVariable String flightId)
    {   //this function removes the pilot from the given flight and automatically assigns a replacement for the flight
        try
        {
            UserDataDTO userDataDTO = pilotFlightAssignmentService.removeFlightFromAPilot(flightId,pilotId);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    //functions for cabin crew and flight assignments
    @GetMapping("/attendant/{attendantId}/getFlights")
    public ResponseEntity<Object> getFlightAssignmentsOfAttendant(@PathVariable int attendantId)
    {   //this function returns the assignments of a given attendant
        try
        {
            UserDataDTO results = attendantAssignmentService.getFlightsOfAttendant(attendantId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getAvailableAttendants")
    public ResponseEntity<Object> getAvailableAttendantsForFlight(@PathVariable String flightId)
    {   //this function returns the available attendants for a given flight
        try
        {
            List<UserDataDTO> results = attendantAssignmentService.getAvailableAttendantsForFlight(flightId);
            return ResponseEntity.ok(results);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getAttendants")
    public ResponseEntity<Object> getAttendantsOfFlight(@PathVariable String flightId)
    {   //this function returns the active attendants for a given flight
        try{

            List<UserDataDTO> userDataDTOList = attendantAssignmentService.getAttendantsOfFlight(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/attendant/{attendantId}/assignToFlight/{flightId}")
    public ResponseEntity<Object> assignAttendantToFlight(@PathVariable int attendantId, @PathVariable String flightId)
    {   //this function assigns the attendant for a given flight and returns the updated attendant information
        try{
            UserDataDTO userDataDTO = attendantAssignmentService.assignAttendantToFlight(flightId, attendantId);
            return  ResponseEntity.ok(userDataDTO);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    

    //functions for passenger and flight assignments

}