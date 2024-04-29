package com.su.FlightScheduler.APIs.PilotAPI;

import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Model.PilotWithLanguages;
import com.su.FlightScheduler.Service.PilotService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.ArrayList;
import java.util.Optional;


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
        //PilotEntity randomPilot = generateRandomPilot();
        // Save the random pilot using the PilotServiceImp
        //PilotEntity savedPilot = pilotService.savePilot(randomPilot);
    }

    @GetMapping("/{pilotId}")
    public ResponseEntity<PilotWithLanguages> getPilotWithId(@PathVariable int pilotId)
    {
        Optional<PilotEntity> pilot = pilotService.findPilotById(pilotId);
        if (pilot.isPresent()) {
            // Return ResponseEntity with status code 200 (OK) and the found pilot entity
            PilotWithLanguages pilotWithLanguages = new PilotWithLanguages(pilot.get());
            return ResponseEntity.ok(pilotWithLanguages);
        } else {
            // Return ResponseEntity with status code 404 (Not Found) if pilot is not found
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/random")
    public ResponseEntity<String> addRandomPilot() {
        // Generate a random pilot (for demonstration purposes)
        PilotEntity randomPilot = generateRandomPilot();
        randomPilot.setPilotId(5);

        System.out.println(randomPilot.getPilotId());
        PilotLanguagePK pilotLanguagePK = new PilotLanguagePK(randomPilot.getPilotId(), "Turkish");
        PilotLanguageEntity pilotLanguageEntity = new PilotLanguageEntity(pilotLanguagePK);


        ArrayList<PilotLanguageEntity> list = new ArrayList<>();
        list.add(pilotLanguageEntity);
        randomPilot.setLanguages(list);


        // Save the random pilot using the PilotServiceImp
        PilotEntity savedPilot = pilotService.savePilot(randomPilot);


        // Return a response indicating the successful addition of the pilot
        return ResponseEntity.status(HttpStatus.CREATED).body("Random pilot added with ID: " + savedPilot.getPilotId());
    }

    // Method to generate a random pilot (for demonstration purposes)
    private PilotEntity generateRandomPilot() {
        PilotEntity pilot = new PilotEntity();
        pilot.setPilotId(2);
        pilot.setEmail("rand@example.com");
        pilot.setFirstName("Yarko");
        pilot.setSurname("Siyez");
        pilot.setAge(31);
        pilot.setAllowedRange(69);
        pilot.setGender("male");
        pilot.setNationality("Turkish");
        pilot.setPilotId(1);
        pilot.setSeniority("junior");
        pilot.setPassword("password");
        return pilot;
    }
}
