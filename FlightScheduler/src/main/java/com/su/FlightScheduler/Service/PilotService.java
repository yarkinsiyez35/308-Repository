package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.List;
import java.util.Optional;

public interface PilotService {

    // Method to save a pilot
    public PilotEntity savePilot(PilotEntity pilot);

    // Method to find a pilot by ID
    public Optional<PilotEntity> findPilotById(int id);

    // Method to find all pilots
    public List<PilotEntity> findAllPilots();

    // Method to update a pilot
    public PilotEntity updatePilot(PilotEntity pilot);

    // Method to delete a pilot by ID
    public void deletePilotById(int id);

}
