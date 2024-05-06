package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CabinCrewServiceImp implements CabinCrewService{

    final CabinCrewRepository cabinCrewRepository;

    public CabinCrewServiceImp(CabinCrewRepository cabinCrewRepository) {
        this.cabinCrewRepository = cabinCrewRepository;
    }

    @Override
    public Optional<CabinCrewEntity> findCabinCrewById(int id) {
        return cabinCrewRepository.findById(id);
    }
}
