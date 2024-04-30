package com.su.FlightScheduler.APIs.PilotAPI;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.DTO.PilotWithLanguagesDTO;
import com.su.FlightScheduler.Service.PilotService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/pilots")
public class PilotController {
    //this controller is responsible for
    //GET List<PilotWithLanguagesDTO> --> returns every pilot in the database
    //GET PilotWithLanguagesDTO --> returns pilot with given id




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


    @GetMapping()
    public ResponseEntity<List<PilotWithLanguagesDTO>> getPilots()
    {
        List<PilotEntity> pilotEntityList = pilotService.findAllPilots();   //find all pilots
        List<PilotWithLanguagesDTO> pilotWithLanguagesDTOList = new ArrayList<>();    //create an empty list to hold the pilots
        for (PilotEntity pilotEntity: pilotEntityList)  //for each PilotEntity
        {
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotEntity);
            pilotWithLanguagesDTOList.add(pilotWithLanguagesDTO);
        }
        return ResponseEntity.ok(pilotWithLanguagesDTOList);
    }

    @GetMapping("/{pilotId}")
    public ResponseEntity<Object> getPilotWithId(@PathVariable int pilotId)
    {
        Optional<PilotEntity> pilot = pilotService.findPilotById(pilotId);
        if (pilot.isPresent()) {
            // Return ResponseEntity with status code 200 (OK) and the found pilot entity
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilot.get());
            return ResponseEntity.ok(pilotWithLanguagesDTO);
        } else {
            // Return ResponseEntity with status code 404 (Not Found) if pilot is not found
            String message = "Pilot with pilotId: " + pilotId + " not found!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> pilotLogin(@RequestBody LoginRequest loginRequest)
    {   //not working
        boolean pilotExists = pilotService.authenticate(loginRequest);
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
