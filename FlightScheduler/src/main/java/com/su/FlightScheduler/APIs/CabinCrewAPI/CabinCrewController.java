package com.su.FlightScheduler.APIs.CabinCrewAPI;


import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.Service.CabinCrewService;
import com.su.FlightScheduler.Service.CabinCrewServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/attendant")
public class CabinCrewController {


    private final CabinCrewService cabinCrewService;
    private final CabinCrewServiceImp cabinCrewServiceImp;

    @Autowired
    public CabinCrewController(CabinCrewService cabinCrewService, CabinCrewServiceImp cabinCrewServiceImp) {
        this.cabinCrewService = cabinCrewService;
        this.cabinCrewServiceImp = cabinCrewServiceImp;
    }


    @GetMapping("/{attendant_id}")
    public ResponseEntity<Object> getCabinCrewWithId(@PathVariable int attendant_id)
    {
        Optional<CabinCrewEntity> cabinCrewEntity = cabinCrewServiceImp.findCabinCrewById(attendant_id);

        if (cabinCrewEntity.isPresent())
        {
            return ResponseEntity.ok(cabinCrewEntity.get());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

}
