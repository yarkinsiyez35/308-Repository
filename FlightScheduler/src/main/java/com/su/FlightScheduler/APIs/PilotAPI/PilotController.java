package com.su.FlightScheduler.APIs.PilotAPI;

import com.su.FlightScheduler.DTO.PilotDTOs.PilotFullDTO;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesDTO;
import com.su.FlightScheduler.Service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/pilots")
public class PilotController {
    //this controller is responsible for
    //<METHOD> <RETURN TYPE> --> <DESCRIPTION>
    //GET List<PilotFullDto> --> returns every pilot in the database
    //GET PilotFullDTO --> returns pilot with given id
    //POST PilotWithLanguagesDTO --> creates a new Pilot and returns it
    //PUT PilotWithLanguagesDTO --> updates an existing Pilot and returns it
    //DELETE PilotWithLanguagesDTO --> deletes a pilot with the given id

    private final PilotService pilotService;
    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping()
    public ResponseEntity<List<PilotFullDTO>> getPilots()
    {
        //find all pilots
        List<PilotEntity> pilotEntityList = pilotService.findAllPilots();
        //create an empty list to hold the pilot DTOs
        List<PilotFullDTO> pilotFullDTOList = new ArrayList<>();
        for (PilotEntity pilotEntity: pilotEntityList)  //for each PilotEntity
        {
            //convert the entity to DTO
            PilotFullDTO pilotFullDTO = new PilotFullDTO(pilotEntity);
            //add the DTO to the list
            pilotFullDTOList.add(pilotFullDTO);
        }
        //return the DTO list
        return ResponseEntity.ok(pilotFullDTOList);
    }

    @GetMapping("/{pilotId}")
    public ResponseEntity<Object> getPilotWithId(@PathVariable int pilotId)
    {
        try
        {
            //find the PilotEntity by id
            PilotEntity pilotEntity = pilotService.findPilotById(pilotId);
            //convert the entity to DTO
            PilotFullDTO pilotFullDTO = new PilotFullDTO(pilotEntity);
            //return the DTO
            return ResponseEntity.ok(pilotFullDTO);
        }
        catch (RuntimeException e)  //this exception is expected
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) //this should not happen
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{pilotId}")
    public ResponseEntity<Object> createPilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
        try
        {
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
        catch (RuntimeException e)  //this exception is expected
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e) //this should not happen
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/{pilotId}")
    public ResponseEntity<Object> updatePilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
       try
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
       catch (RuntimeException e)   //this exception is expected
       {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
       catch (Exception e) //this should not happen
       {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
    }

    @DeleteMapping("/{pilotId}")
    public ResponseEntity<Object> deletePilotWithId(@PathVariable int pilotId)
    {
        try
        {
            //delete the entity
            PilotEntity pilotEntity = pilotService.deletePilotById(pilotId);
            //convert the deleted entity to DTO
            PilotWithLanguagesDTO deletedPilotDTO = new PilotWithLanguagesDTO(pilotEntity);
            //return the deleted entity as DTO
            return ResponseEntity.ok(deletedPilotDTO);
        }
        catch (RuntimeException e)   //this exception is expected
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) //this should not happen
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
