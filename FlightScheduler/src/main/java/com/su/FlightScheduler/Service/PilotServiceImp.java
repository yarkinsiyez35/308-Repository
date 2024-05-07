package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Repository.PilotLanguageRepository;
import com.su.FlightScheduler.Repository.PilotRepository;
import com.su.FlightScheduler.Entity.PilotEntity;
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
    {
        PilotEntity savedPilot;
        if (pilot.getLanguages() != null)   //if pilot has languages
        {
            //create the same entity without the languages
            PilotEntity newPilot = new PilotEntity(pilot);
            //save the entity without languages
            savedPilot = pilotRepository.save(newPilot);
            //save the languages to the PilotLanguageEntity table
            List<PilotLanguageEntity> savedPilotLanguageEntityList = pilotLanguageRepository.saveAll(pilot.getLanguages());
            //add the saved languages to the saved PilotLanguageEntity
            savedPilot.setLanguages(savedPilotLanguageEntityList);  //add the languages to the returned object
        }
        else
        {
            //save the entity
            savedPilot = pilotRepository.save(pilot);
        }
        //return saved entity
        return savedPilot;
    }
    public Optional<PilotEntity> findPilotById(int id) {
        return pilotRepository.findById(id);
    }

    public List<PilotEntity> findAllPilots() {
        return pilotRepository.findAll();
    }

    public PilotEntity updatePilot(PilotEntity pilot) {
        PilotEntity updatedPilot;
        if (pilot.getLanguages() != null)
        {
            //get previous languages
            List<PilotLanguageEntity> oldPilotLanguageEntities = pilotLanguageRepository.findPilotLanguageEntitiesByPilotLanguagePK_PilotId(pilot.getPilotId());
            //delete previous languages
            pilotLanguageRepository.deleteAll(oldPilotLanguageEntities);
            //save new languages
            List<PilotLanguageEntity> updatedPilotLanguageEntityList = pilotLanguageRepository.saveAll(pilot.getLanguages());
            //create the same entity without the languages
            PilotEntity toBeUpdatedPilot = new PilotEntity(pilot);
            //save the entity
            updatedPilot = pilotRepository.save(toBeUpdatedPilot);
            updatedPilot.setLanguages(updatedPilotLanguageEntityList);
        }
        else
        {
            //save the entity
            updatedPilot = pilotRepository.save(pilot);
        }
        //return updated entity
        return updatedPilot;
    }

    public void deletePilotById(int id) {
        pilotRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(LoginRequest loginRequest) {
        //check if a pilot exists with given email and password
        Optional<PilotEntity> pilotEntity = pilotRepository.findPilotEntityByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        //return true if it exists
        return pilotEntity.isPresent();
    }
}
