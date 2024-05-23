package com.su.FlightScheduler.APIs;


import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Service.PilotFlightAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("test/assignment")
public class PilotFlightAssignmentController {
    //this controller is created for testing the functions in the service, the codes will be merged to the actualy main controller


    PilotFlightAssignmentService pilotFlightAssignmentService;

    @Autowired
    public PilotFlightAssignmentController(PilotFlightAssignmentService pilotFlightAssignmentService) {
        this.pilotFlightAssignmentService = pilotFlightAssignmentService;
    }

    @GetMapping("/pilot/{pilotId}")
    public ResponseEntity<Object> getFlightAssignmentsOfAPilot(@PathVariable int pilotId)
    {
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

    @GetMapping("/flight/{flightId}/available")
    public ResponseEntity<Object> getAvailablePilotsForAFlight(@PathVariable String flightId)
    {
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

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Object> getPilotsOfAFlight(@PathVariable String flightId)
    {
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


    @PostMapping("/{pilotId}/{flightId}")
    public ResponseEntity<Object> assignPilotToAFlight(@PathVariable int pilotId, @PathVariable String flightId)
    {
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
}
