package com.su.FlightScheduler.APIs.CabinCrewAPI;


import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Service.AttendantAssignmentService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("test/cabinCrewAssignment")

public class CabinCrewFlightAssignmentContoller {

    AttendantAssignmentService attendantAssignmentService;

    @Autowired
    public CabinCrewFlightAssignmentContoller(AttendantAssignmentService attendantAssignmentService){
        this.attendantAssignmentService = attendantAssignmentService;
    }

    @GetMapping("/attendant/{attendantId}")
    public ResponseEntity<Object> getFlightAssignmentsOfAttendant(@PathVariable int attendantId){

        try{
            UserDataDTO results = attendantAssignmentService.getFlightsOfAttendant(attendantId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/available")
    public ResponseEntity<Object> getAvailableAttendantsForAFlight(@PathVariable String flightId){
        try{
            List<UserDataDTO> results = attendantAssignmentService.getAttendantsOfFlight(flightId);
            return ResponseEntity.ok(results);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Object> getAttendantsOfAFlight(@PathVariable String flightId){

        try{

            List<UserDataDTO> userDataDTOList = attendantAssignmentService.getAttendantsOfFlight(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{attendantId}/{flightId}")
    public ResponseEntity<Object> assignAttendantToAFlight(@PathVariable int attendantId, @PathVariable String flightId){

        try{
            UserDataDTO userDataDTO = attendantAssignmentService.assignAttendantToFlight(flightId, attendantId);
            return  ResponseEntity.ok(userDataDTO);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }


}


