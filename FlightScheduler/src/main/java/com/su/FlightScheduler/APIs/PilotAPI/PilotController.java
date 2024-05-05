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
    //<METHOD> <RETURN TYPE> --> <DESCRIPTION>
    //GET List<PilotWithLanguagesDTO> --> returns every pilot in the database
    //GET PilotWithLanguagesDTO --> returns pilot with given id
    //POST PilotWithLanguagesDTO --> creates a new Pilot and returns it
    //DELETE PilotWithLanguagesDTO --> deletes a pilot with the given id



    private final PilotService pilotService;
    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
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
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilot.get());
            return ResponseEntity.ok(pilotWithLanguagesDTO);
        } else {
            String message = "Pilot with pilotId: " + pilotId + " not found!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping("/{pilotId}")
    public ResponseEntity<Object> postPilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
        if (pilotService.findPilotById(pilotId).isPresent())
        {
            String message = "Pilot with pilotId: " + pilotId + " already exists!";
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
        }
        pilotWithLanguagesDTO.setPilotId(pilotId);  //prevention against giving different pilotId in the dto
        PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO);
        PilotEntity savedPilot = pilotService.savePilot(pilotEntity);
        PilotWithLanguagesDTO savedPilotDTO = new PilotWithLanguagesDTO(savedPilot);
        return ResponseEntity.ok(savedPilotDTO);
    }

    @DeleteMapping("/{pilotId}")
    public ResponseEntity<Object> deletePilotWithId(@PathVariable int pilotId)
    {
        Optional<PilotEntity> pilotEntity = pilotService.findPilotById(pilotId);
        if (pilotEntity.isPresent())
        {
            pilotService.deletePilotById(pilotId);
            PilotWithLanguagesDTO deletedPilot = new PilotWithLanguagesDTO(pilotEntity.get());
            return ResponseEntity.ok(deletedPilot);
        }
        else
        {
            String message = "Pilot with pilotId: " + pilotId + " cannot be deleted!";
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
