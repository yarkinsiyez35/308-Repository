package com.su.FlightScheduler.APIs.PilotAPI;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotFullDTO;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesAsStringDTO;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesDTO;
import com.su.FlightScheduler.Service.PilotService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.ArrayList;
import java.util.List;

//TESTING: this controller should be tested, but not now
@RestController
@RequestMapping("/api/pilots")
@CrossOrigin(value = "http://127.0.0.1:5500", allowCredentials = "true")
public class PilotController {
    //this controller is responsible for
    //<METHOD> <RETURN TYPE> --> <DESCRIPTION>
    //GET List<PilotWithLanguagesAsStringDTO> --> returns every pilot in the database
    //GET PilotWithLanguagesAsStringDTO --> returns pilot with given id
    //POST PilotWithLanguagesAsStringDTO --> creates a new Pilot and returns it
    //PUT PilotWithLanguagesAsStringDTO --> updates an existing Pilot and returns it
    //DELETE PilotWithLanguagesAsStringDTO --> deletes a pilot with the given id

    private final PilotService pilotService;
    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping()
    public ResponseEntity<List<PilotWithLanguagesAsStringDTO>> getPilots()
    {
        //find all pilots
        List<PilotEntity> pilotEntityList = pilotService.findAllPilots();
        //create an empty list to hold the pilot DTOs
        List<PilotWithLanguagesAsStringDTO> pilotWithLanguagesAsStringDTOList = new ArrayList<>();
        for (PilotEntity pilotEntity: pilotEntityList)  //for each PilotEntity
        {
            //convert the entity to DTO
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotEntity);
            PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO(pilotWithLanguagesDTO);
            //add the DTO to the list
            pilotWithLanguagesAsStringDTOList.add(pilotWithLanguagesAsStringDTO);
        }
        //return the DTO list
        return ResponseEntity.ok(pilotWithLanguagesAsStringDTOList);
    }

    @GetMapping("/{pilotId}")
    public ResponseEntity<Object> getPilotWithId(@PathVariable int pilotId)
    {
        try
        {
            //find the PilotEntity by id
            PilotEntity pilotEntity = pilotService.findPilotById(pilotId);
            //convert the entity to DTO
            UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_with_pilot_entity(pilotEntity);
            //return the DTO
            return ResponseEntity.ok(userDataDTO);
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
    public ResponseEntity<Object> createPilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO)
    {
        try
        {
            //make sure the id's match
            pilotWithLanguagesAsStringDTO.setPilotId(pilotId);
            //convert the input DTO to another DTO
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotWithLanguagesAsStringDTO);
            //create PilotEntity from the input DTO
            PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO);
            //save the PilotEntity
            PilotEntity savedPilot = pilotService.savePilot(pilotEntity);
            //convert the saved PilotEntity to DTO
            PilotWithLanguagesDTO savedPilotDTO = new PilotWithLanguagesDTO(savedPilot);
            //convert DTO to another DTO
            UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_data_with_pilotWithLanguagesDTO(savedPilotDTO);
            //return the DTO
            return ResponseEntity.ok(userDataDTO);
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

    @PostMapping("/createPilot")
    public ResponseEntity<Object> createPilotWithoutId(@RequestBody PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO)
    {
        //bug here fix it
        try
        {
            //allowedRange should come as integer
            //languages come as null

            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotWithLanguagesAsStringDTO);
            PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO, false);
            PilotEntity savedPilot = pilotService.savePilotWithoutId(pilotEntity);
            PilotWithLanguagesDTO savedPilotWithLanguagesDTO = new PilotWithLanguagesDTO(savedPilot);
            PilotWithLanguagesAsStringDTO savedPilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO(savedPilotWithLanguagesDTO);
            return ResponseEntity.ok(savedPilotWithLanguagesAsStringDTO);
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
    public ResponseEntity<Object> updatePilotWithId(@PathVariable int pilotId, @RequestBody PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO)
    {
       try
       {
            //make sure the id's match
            pilotWithLanguagesAsStringDTO.setPilotId(pilotId);
            //convert DTO to another DTO
            PilotWithLanguagesDTO pilotWithLanguagesDTO = new PilotWithLanguagesDTO(pilotWithLanguagesAsStringDTO);
            //create PilotEntity from the input DTO
            PilotEntity pilotEntity = new PilotEntity(pilotWithLanguagesDTO);
            //update the PilotEntity
            PilotEntity updatedPilot = pilotService.updatePilot(pilotEntity);
            //convert the updated PilotEntity to DTO
            PilotWithLanguagesDTO updatedPilotDTO = new PilotWithLanguagesDTO(updatedPilot);
            //convert DTO to another DTO
            PilotWithLanguagesAsStringDTO updatedPilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO(updatedPilotDTO);
            //return the DTO
            return ResponseEntity.ok(updatedPilotWithLanguagesAsStringDTO);
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
            //convert DTO to another DTO
            PilotWithLanguagesAsStringDTO pilotWithLanguagesAsStringDTO = new PilotWithLanguagesAsStringDTO(deletedPilotDTO);
            //return the deleted entity as DTO
            return ResponseEntity.ok(pilotWithLanguagesAsStringDTO);
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
