package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotLanguageRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Entity.PilotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TESTING: this service should be tested
@Service
@Transactional
public class PilotServiceImp implements PilotService {


    private final PilotRepository pilotRepository;
    private final PilotLanguageRepository pilotLanguageRepository;

    @Autowired
    public PilotServiceImp(PilotRepository pilotRepository, PilotLanguageRepository pilotLanguageRepository) {
        this.pilotRepository = pilotRepository;
        this.pilotLanguageRepository = pilotLanguageRepository;
    }

    @Override
    public PilotEntity savePilot(PilotEntity pilot)
    {
        if (pilotExistsById(pilot.getPilotId()))    //cannot create a pilot with an existing id
        {
            throw new RuntimeException("Pilot with id " + pilot.getPilotId() + " cannot be created!");
        }
        if (pilotRepository.findPilotEntityByEmail(pilot.getEmail()).isPresent())   //cannot create a pilot with another existing email address
        {
            throw new RuntimeException("Pilot with email " + pilot.getEmail() + " cannot be created!");
        }

        PilotEntity savedPilot;
        if (pilot.getLanguages() != null)   //if pilot has languages
        {
            //create the same entity without the languages
            PilotEntity newPilot = new PilotEntity(pilot);
            //save the entity without languages
            savedPilot = pilotRepository.save(new PilotEntity(pilot));
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

    @Override
    public PilotEntity savePilotWithoutId(PilotEntity pilotEntity) throws RuntimeException {
        if (pilotRepository.findPilotEntityByEmail(pilotEntity.getEmail()).isPresent())   //cannot create a pilot with another existing email address
        {
            throw new RuntimeException("Pilot with email " + pilotEntity.getEmail() + " cannot be created!");
        }

        PilotEntity savedPilot;
        if (pilotEntity.getLanguages() != null)   //if pilot has languages
        {
            //create the same entity without the languages
            PilotEntity newPilot = new PilotEntity(pilotEntity);
            //save the entity without languages
            savedPilot = pilotRepository.save(new PilotEntity(pilotEntity));
            //set the pilot id of the languages
            List<PilotLanguageEntity> languagesToBeSaved = pilotEntity.getLanguages();
            for (PilotLanguageEntity pilotLanguageEntity : languagesToBeSaved)
            {
                PilotLanguagePK pilotLanguagePK = new PilotLanguagePK(savedPilot.getPilotId(), pilotLanguageEntity.getPilotLanguagePK().getLanguage());
                pilotLanguageEntity.setPilotLanguagePK(pilotLanguagePK);
            }
            //save the languages to the PilotLanguageEntity table
            List<PilotLanguageEntity> savedPilotLanguageEntityList = pilotLanguageRepository.saveAll(pilotEntity.getLanguages());
            //add the saved languages to the saved PilotLanguageEntity
            savedPilot.setLanguages(savedPilotLanguageEntityList);  //add the languages to the returned object
        }
        else
        {
            //save the entity
            savedPilot = pilotRepository.save(pilotEntity);
        }
        //return saved entity
        return savedPilot;
    }

    @Override
    public PilotEntity findPilotById(int id)
    {
        //get pilotEntity from the repository
        PilotEntity pilotEntity = pilotRepository.findById(id).orElseThrow(() -> new RuntimeException("Pilot with id: " + id + " does not exist!"));
        return pilotEntity;
    }

    @Override
    public boolean pilotExistsById(int id)
    {
        return pilotRepository.existsById(id);
    }

    @Override
    public List<PilotEntity> findAllPilots()
    {
        return pilotRepository.findAll();
    }

    @Override
    public PilotEntity updatePilot(PilotEntity pilot)
    {
        if (!pilotExistsById(pilot.getPilotId()))   //a nonexistent pilot cannot be updated
        {
            throw new RuntimeException("Pilot with id: " + pilot.getPilotId() + " cannot be updated!");
        }
        if (pilotRepository.findPilotEntityByEmail(pilot.getEmail()).isPresent() && pilot.getPilotId() != pilotRepository.findPilotEntityByEmail(pilot.getEmail()).get().getPilotId())
        {
            throw new RuntimeException("Pilot with email " + pilot.getEmail() + " cannot be updated!");
        }
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

    @Override
    public PilotEntity deletePilotById(int id)
    {
        try //findPilotById() throws exception
        {
            //find the pilotEntity to delete
            PilotEntity pilotEntity = findPilotById(id);
            //delete the pilotEntity
            pilotRepository.deleteById(id);
            //return the deleted pilotEntity
            return pilotEntity;
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("Pilot with id: " + id + " cannot be deleted!");
        }
    }
}
