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
    //PUT PilotWithLanguagesDTO --> updates an existing Pilot and returns it
    //DELETE PilotWithLanguagesDTO --> deletes a pilot with the given id

    private final PilotService pilotService;
    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping()
    public ResponseEntity<List<PilotWithLanguagesDTO>> getPilots()
    {
        //find all pilots
        List<PilotEntity> pilotEntityList = pilotService.findAllPilots();
        //create an empty list to hold the pilot DTOs
        List<PilotWithLanguagesDTO> pilotWithLanguagesDTOList = new ArrayList<>();
        for (PilotEntity pilotEntity: pilotEntityList)  //for each PilotEntity
        {
            //convert the entity to DTO
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotEntity);
            //add the DTO to the list
            pilotWithLanguagesDTOList.add(pilotWithLanguagesDTO);
        }
        //return the DTO list
        return ResponseEntity.ok(pilotWithLanguagesDTOList);
    }

    @GetMapping("/{pilotId}")
    public ResponseEntity<Object> getPilotWithId(@PathVariable int pilotId)
    {
        //find the PilotEntity by id
        Optional<PilotEntity> pilot = pilotService.findPilotById(pilotId);
        if (pilot.isPresent())  //an entity exists with given id
        {
            //convert the entity to DTO
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilot.get());
            //return the DTO
            return ResponseEntity.ok(pilotWithLanguagesDTO);
        }
        else
        {
            //create error message
            String message = "Pilot with pilotId: " + pilotId + " not found!";
            //return error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping("/{pilotId}")
    public ResponseEntity<Object> postPilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
        if (pilotService.findPilotById(pilotId).isPresent())    //an entity exists with given id
        {
            //create error message
            String message = "Pilot with pilotId: " + pilotId + " already exists!";
            //return error message
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
        }
        //make sure the id's match
        pilotWithLanguagesDTO.setPilotId(pilotId);
        //create PilotEntity from the input DTO
        PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO);
        //save the PilotEntity
        PilotEntity savedPilot = pilotService.savePilot(pilotEntity);
        //convert the saved PilotEntity to DTO
        PilotWithLanguagesDTO savedPilotDTO = new PilotWithLanguagesDTO(savedPilot);
        //return the DTO
        return ResponseEntity.ok(savedPilotDTO);
    }

    @PutMapping("/{pilotId}")
    public ResponseEntity<Object> updatePilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
        if (pilotService.findPilotById(pilotId).isEmpty())  //pilot with given id does not exist
        {
            //create error message
            String message = "Pilot with pilotId: " + pilotId + " not found!";
            //return error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        else    //pilot with given id exists
        {
            //make sure the id's match
            pilotWithLanguagesDTO.setPilotId(pilotId);
            //create PilotEntity from the input DTO
            PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO);
            //update the PilotEntity
            PilotEntity updatedPilot = pilotService.updatePilot(pilotEntity);
            //convert the updated PilotEntity to DTO
            PilotWithLanguagesDTO updatedPilotDTO = new PilotWithLanguagesDTO(updatedPilot);
            //return the DTO
            return ResponseEntity.ok(updatedPilotDTO);
        }
    }

    @DeleteMapping("/{pilotId}")
    public ResponseEntity<Object> deletePilotWithId(@PathVariable int pilotId)
    {
        //find PilotEntity by given id
        Optional<PilotEntity> pilotEntity = pilotService.findPilotById(pilotId);
        if (pilotEntity.isPresent())    //entity exists
        {
            //delete the entity
            pilotService.deletePilotById(pilotId);
            //convert the deleted entity to DTO
            PilotWithLanguagesDTO deletedPilotDTO = new PilotWithLanguagesDTO(pilotEntity.get());
            //return the deleted entity as DTO
            return ResponseEntity.ok(deletedPilotDTO);
        }
        else    //entity does not exist
        {
            //create error message
            String message = "Pilot with pilotId: " + pilotId + " cannot be deleted!";
            //return error message
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
