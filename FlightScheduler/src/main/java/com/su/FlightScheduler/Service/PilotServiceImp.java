package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Repository.PilotRepository;
import com.su.FlightScheduler.Entity.PilotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PilotServiceImp implements PilotService{

    private final PilotRepository pilotRepository;

    @Autowired
    public PilotServiceImp(PilotRepository pilotRepository) {
        this.pilotRepository = pilotRepository;
    }

    // Method to save a pilot
    public PilotEntity savePilot(PilotEntity pilot) {
        return pilotRepository.save(pilot);
    }

    // Method to find a pilot by ID
    public Optional<PilotEntity> findPilotById(int id) {
        return pilotRepository.findById(id);
    }

    // Method to find all pilots
    public List<PilotEntity> findAllPilots() {
        return pilotRepository.findAll();
    }

    // Method to update a pilot
    public PilotEntity updatePilot(PilotEntity pilot) {
        return pilotRepository.save(pilot);
    }

    // Method to delete a pilot by ID
    public void deletePilotById(int id) {
        pilotRepository.deleteById(id);
    }
}
