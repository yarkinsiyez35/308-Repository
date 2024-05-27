package com.su.FlightScheduler.APIs.CabinCrewAPI;


import com.su.FlightScheduler.DTO.CabinCrewDTOs.AttendantWithLanguagesAsStringDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.DTO.CabinCrewDTOs.AttendantWithLanguagesDTO;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Service.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//TESTING: this controller should be tested, but not now
@RestController
@RequestMapping("/api/attendants")
@CrossOrigin(value = "http://127.0.0.1:5500", allowCredentials = "true")
public class CabinCrewController {



    private final CabinCrewRepository cabinCrewRepository;
    private AttendantService attendantService;
    @Autowired
    public CabinCrewController(AttendantService attendantService, CabinCrewRepository cabinCrewRepository) {
        this.attendantService = attendantService;
        this.cabinCrewRepository = cabinCrewRepository;
    }

    @GetMapping()
    public ResponseEntity<List<AttendantWithLanguagesAsStringDTO>> getAllCabinCrew() {

        List<CabinCrewEntity> cabinCrewEntityList  = attendantService.findAllCabinCrew();

        List<AttendantWithLanguagesAsStringDTO> attendantWithLanguagesAsStringDTOList = new ArrayList<>();

        for (CabinCrewEntity cabinCrewEntity : cabinCrewEntityList) {

            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(cabinCrewEntity);
            attendantWithLanguagesAsStringDTOList.add(new AttendantWithLanguagesAsStringDTO(attendantWithLanguagesDTO));
        }
        return ResponseEntity.ok(attendantWithLanguagesAsStringDTOList);
    }

    @GetMapping("/{attendantId}")
    public ResponseEntity<Object> getAttendantById(@PathVariable int attendantId) {

        try {
            CabinCrewEntity cabinCrewEntity = attendantService.findAttendantById(attendantId);
            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(cabinCrewEntity);
            AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO = new AttendantWithLanguagesAsStringDTO(attendantWithLanguagesDTO);
            return ResponseEntity.ok(attendantWithLanguagesAsStringDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{attendantId}")
    public ResponseEntity<Object> createAttendantWithId(@PathVariable int attendantId, @RequestBody AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO) {

        try{
            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(attendantWithLanguagesAsStringDTO);
            attendantWithLanguagesDTO.setAttendantId(attendantId);
            CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(attendantWithLanguagesDTO);
            CabinCrewEntity savedAttendant = attendantService.saveCabin(cabinCrewEntity);
            AttendantWithLanguagesDTO savedAttendantDTO = new AttendantWithLanguagesDTO(savedAttendant);
            AttendantWithLanguagesAsStringDTO savedAttendantWithLanguagesAsStringDTO = new AttendantWithLanguagesAsStringDTO(savedAttendantDTO);
            return ResponseEntity.ok(savedAttendantWithLanguagesAsStringDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/createAttendant")
    public  ResponseEntity<Object> createAttendantWithoutId(@RequestBody AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO){
        try
        {

            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(attendantWithLanguagesAsStringDTO);
            CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(attendantWithLanguagesDTO, false);
            CabinCrewEntity savedCabinCrew = attendantService.saveAttendantWithoutId(cabinCrewEntity);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_with_cabin_crew_entity(savedCabinCrew);
            return ResponseEntity.ok(userDataDTO);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @PutMapping("/{attendantId}")
    public ResponseEntity<Object> updateAttendantWithId(@PathVariable int attendantId, @RequestBody AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO){

        try{

            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(attendantWithLanguagesAsStringDTO);

            attendantWithLanguagesDTO.setAttendantId(attendantId);

            CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(attendantWithLanguagesDTO, true);

            CabinCrewEntity updatedAttendant = attendantService.updateCabin(cabinCrewEntity);

            AttendantWithLanguagesDTO updatedAttendantDTO = new AttendantWithLanguagesDTO(updatedAttendant);

            AttendantWithLanguagesAsStringDTO updatedAttendantWithLanguagesAsStringDTO = new AttendantWithLanguagesAsStringDTO(updatedAttendantDTO);
            return ResponseEntity.ok(updatedAttendantWithLanguagesAsStringDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/login")
    public ResponseEntity<Object> deleteAttendantById(@PathVariable int attendantId) {

        try{

            CabinCrewEntity cabinCrewEntity = attendantService.deleteCabinById(attendantId);

            AttendantWithLanguagesDTO deletedAttendantDTO = new AttendantWithLanguagesDTO(cabinCrewEntity);

            AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO = new AttendantWithLanguagesAsStringDTO(deletedAttendantDTO);
            return ResponseEntity.ok(attendantWithLanguagesAsStringDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> attendantLogin(@RequestBody LoginRequest loginRequest) {

        boolean attendantExits = attendantService.authenticate(loginRequest);

        if (attendantExits) {

            return ResponseEntity.ok(loginRequest);

        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



}
