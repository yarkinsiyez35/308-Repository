package com.su.FlightScheduler.APIs.CabinCrewAPI;


import com.su.FlightScheduler.DTO.LoginDTO;
import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.DTO.AttendantWithLanguagesDTO;
import com.su.FlightScheduler.Repository.CabinCrewRepository;
import com.su.FlightScheduler.Service.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/attendants")
public class CabinCrewController {

    //repoları servis yap

    private final CabinCrewRepository cabinCrewRepository;
    private AttendantService attendantService;
    @Autowired
    public CabinCrewController(AttendantService attendantService, CabinCrewRepository cabinCrewRepository) {
        this.attendantService = attendantService;
        this.cabinCrewRepository = cabinCrewRepository;
    }

    @GetMapping()
    public ResponseEntity<List<AttendantWithLanguagesDTO>> getAllCabinCrew() {

        List<CabinCrewEntity> cabinCrewEntityList  = attendantService.findAllCabinCrew();

        List<AttendantWithLanguagesDTO> attendantWithLanguagesDTOList = new ArrayList<>();

        for (CabinCrewEntity cabinCrewEntity : cabinCrewEntityList) {

            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(cabinCrewEntity);

            attendantWithLanguagesDTOList.add(attendantWithLanguagesDTO);
        }

        return ResponseEntity.ok(attendantWithLanguagesDTOList);
    }

    @GetMapping("/{attendantId}")
    public ResponseEntity<Object> getAttendatById(@PathVariable int attendantId) {

        try {

            CabinCrewEntity cabinCrewEntity = attendantService.findAttendantById(attendantId);

            AttendantWithLanguagesDTO attendantWithLanguagesDTO = new AttendantWithLanguagesDTO(cabinCrewEntity);

            return ResponseEntity.ok(attendantWithLanguagesDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{attendantId}")
    public ResponseEntity<Object> createAttendantWithId(@PathVariable int attendantId, @RequestBody AttendantWithLanguagesDTO attendantWithLanguagesDTO) {

        try{

            attendantWithLanguagesDTO.setAttendantId(attendantId);

            CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(attendantWithLanguagesDTO);

            CabinCrewEntity savedAttendant = attendantService.saveCabin(cabinCrewEntity);

            AttendantWithLanguagesDTO savedAttendantDTO = new AttendantWithLanguagesDTO(savedAttendant);

            return ResponseEntity.ok(savedAttendantDTO);
        }
        catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{attendantId}")
    public ResponseEntity<Object> updateAttendantWithId(@PathVariable int attendantId, @RequestBody AttendantWithLanguagesDTO attendantWithLanguagesDTO){

        try{


            attendantWithLanguagesDTO.setAttendantId(attendantId);

            CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(attendantWithLanguagesDTO);

            CabinCrewEntity updatedAttendant = attendantService.updateCabin(cabinCrewEntity);

            AttendantWithLanguagesDTO updatedAttendantDTO = new AttendantWithLanguagesDTO(updatedAttendant);

            return ResponseEntity.ok(updatedAttendantDTO);
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

            return ResponseEntity.ok(deletedAttendantDTO);
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
