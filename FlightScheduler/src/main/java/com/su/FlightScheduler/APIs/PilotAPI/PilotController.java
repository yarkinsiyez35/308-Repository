package com.su.FlightScheduler.APIs.PilotAPI;

import com.su.FlightScheduler.Service.PilotService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.su.FlightScheduler.Entity.PilotEntity;


@RestController
@RequestMapping("/api/pilots")
public class PilotController {
    private final PilotService pilotService;
    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @PostConstruct
    private void addPilot()
    {
        PilotEntity randomPilot = generateRandomPilot();
        // Save the random pilot using the PilotServiceImp
        PilotEntity savedPilot = pilotService.savePilot(randomPilot);
    }

    @PostMapping("/random")
    public ResponseEntity<String> addRandomPilot() {
        // Generate a random pilot (for demonstration purposes)
        System.out.println("HERE");
        PilotEntity randomPilot = generateRandomPilot();

        // Save the random pilot using the PilotServiceImp
        PilotEntity savedPilot = pilotService.savePilot(randomPilot);

        // Return a response indicating the successful addition of the pilot
        return ResponseEntity.status(HttpStatus.CREATED).body("Random pilot added with ID: " + savedPilot.getPilotId());
    }

    // Method to generate a random pilot (for demonstration purposes)
    private PilotEntity generateRandomPilot() {
        PilotEntity pilot = new PilotEntity();
        // Set random values for pilot attributes
        pilot.setPilotId(2);
        pilot.setEmail("random@example.com");
        pilot.setFirstName("Random");
        pilot.setSurname("Pilot");
        pilot.setAge(30);
        pilot.setAllowedRange(2000);
        pilot.setGender("male");
        pilot.setNationality("Turkish");
        pilot.setPilotId(1);
        pilot.setSeniority("senior");
        pilot.setPassword("password");
        // Set other attributes as needed
        return pilot;
    }
}
