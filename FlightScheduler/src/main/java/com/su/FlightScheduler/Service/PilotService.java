package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.List;
import java.util.Optional;

public interface PilotService {

    // Method to save a pilot
    public PilotEntity savePilot(PilotEntity pilot) throws RuntimeException;

    // Method to find a pilot by ID
    public PilotEntity findPilotById(int id) throws RuntimeException;
    public boolean pilotExistsById(int id);

    // Method to find all pilots
    public List<PilotEntity> findAllPilots();

    // Method to update a pilot
    public PilotEntity updatePilot(PilotEntity pilot) throws RuntimeException;

    // Method to delete a pilot by ID
    public PilotEntity deletePilotById(int id) throws RuntimeException;
}
