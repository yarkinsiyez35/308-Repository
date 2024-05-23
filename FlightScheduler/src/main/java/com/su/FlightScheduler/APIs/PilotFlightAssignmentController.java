package com.su.FlightScheduler.APIs;


import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Service.PilotFlightAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test/assignment")
public class PilotFlightAssignmentController {
    //this controller is created for testing the functions in the service, the codes will be merged to the actualy main controller


    PilotFlightAssignmentService pilotFlightAssignmentService;

    @Autowired
    public PilotFlightAssignmentController(PilotFlightAssignmentService pilotFlightAssignmentService) {
        this.pilotFlightAssignmentService = pilotFlightAssignmentService;
    }



    @GetMapping("/{pilotId}")
    ResponseEntity<Object> getFlightAssignmentsOfAPilot(@PathVariable int pilotId)
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
}
