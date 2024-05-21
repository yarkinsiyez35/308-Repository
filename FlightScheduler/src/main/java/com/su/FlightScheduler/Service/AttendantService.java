package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AttendantService {






    // Method to save a Cabin Crew member
    public CabinCrewEntity saveCabin(CabinCrewEntity cabin) throws RuntimeException;

    // Method to find Cabin Crew member by ID
    public CabinCrewEntity findAttendantById(int id) throws RuntimeException;

    public boolean cabinCrewExistsById(int id);

    // Method to find all Cabin Crew members
    public  List<CabinCrewEntity> findAllCabinCrew();

    // Method to update a Cabin Crew member
    public CabinCrewEntity updateCabin(CabinCrewEntity cabin) throws RuntimeException;

    // Method to delete a Cabin Crew member by ID
    public CabinCrewEntity deleteCabinById(int id) throws RuntimeException;

    public boolean authenticate(LoginRequest loginRequest);




}
