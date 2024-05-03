package com.su.FlightScheduler.Service;


import com.su.FlightScheduler.Entity.CabinCrewEntity;

import java.util.Optional;

public interface CabinCrewService {

    public Optional<CabinCrewEntity> findCabinCrewById(int id);
}
