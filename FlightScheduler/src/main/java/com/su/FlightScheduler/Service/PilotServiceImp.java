package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Repository.PilotLanguageRepository;
import com.su.FlightScheduler.Repository.PilotRepository;
import com.su.FlightScheduler.Entity.PilotEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PilotServiceImp implements PilotService {


    private final PilotRepository pilotRepository;
    private final PilotLanguageRepository pilotLanguageRepository;

    @Autowired
    public PilotServiceImp(PilotRepository pilotRepository, PilotLanguageRepository pilotLanguageRepository) {
        this.pilotRepository = pilotRepository;
        this.pilotLanguageRepository = pilotLanguageRepository;
    }

    public PilotEntity savePilot(PilotEntity pilot)     //there must be a better way
    {   //add try catch

        PilotEntity savedPilot;
        if (pilot.getLanguages() != null)
        {
            List<PilotLanguageEntity> pilotLanguageEntities = pilot.getLanguages();
            PilotEntity newPilot = new PilotEntity(pilot);
            savedPilot = pilotRepository.save(newPilot);
            List<PilotLanguageEntity> savedPilotLanguageEntities = pilotLanguageRepository.saveAll(pilot.getLanguages());
            savedPilot.setLanguages(savedPilotLanguageEntities);  //add the languages to the returned object
        }
        else
        {
            savedPilot = pilotRepository.save(pilot);
        }
        return savedPilot;
    }

    public Optional<PilotEntity> findPilotById(int id) {
        return pilotRepository.findById(id);
    }

    public List<PilotEntity> findAllPilots() {
        return pilotRepository.findAll();
    }

    public PilotEntity updatePilot(PilotEntity pilot) {
        return pilotRepository.save(pilot);
    }

    public void deletePilotById(int id) {
        pilotRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(LoginRequest loginRequest) {
        Optional<PilotEntity> pilotEntity = pilotRepository.findPilotEntityByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return pilotEntity.isPresent();
    }
}
