package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.List;

public interface AttendantService {

    // Method to save a Cabin Crew member
    public CabinCrewEntity saveCabin(CabinCrewEntity cabin) throws RuntimeException;

    public CabinCrewEntity saveAttendantWithoutId(CabinCrewEntity cabinCrewEntity) throws RuntimeException;

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
