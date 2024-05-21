package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.Entity.DishRecipeEntity;
import com.su.FlightScheduler.Entity.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.AttendantLanguagePK;
import com.su.FlightScheduler.Entity.DishRecipePK;
import com.su.FlightScheduler.Entity.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewAssignmentsPK;
import com.su.FlightScheduler.Entity.AttendantVehicleTypeEntity;
import com.su.FlightScheduler.Entity.AttendantVehicleTypePK;

import com.su.FlightScheduler.Repository.CabinCrewRepository;
import com.su.FlightScheduler.Repository.CabinLanguageRepository;
import com.su.FlightScheduler.Repository.CabinVehicleTypeRepository;
import com.su.FlightScheduler.Repository.CabinAssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.FlightScheduler.Service.AttendantService;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CyclicBarrier;

@Service
@Transactional
public class AttendantServiceImp implements AttendantService {

    private final CabinCrewRepository cabinCrewRepository;
    private final CabinLanguageRepository cabinLanguageRepository;


    @Autowired
    public AttendantServiceImp(CabinCrewRepository cabinCrewRepository, CabinLanguageRepository cabinLanguageRepository) {
        this.cabinCrewRepository = cabinCrewRepository;
        this.cabinLanguageRepository = cabinLanguageRepository;
    }

    @Override
    public CabinCrewEntity saveCabin(CabinCrewEntity cabin){

        if (cabinCrewRepository.existsById(cabin.getAttendantId())){

            throw new RuntimeException("Cabin Crew with id " + cabin.getAttendantId() + " cannot be created!");
        }

        CabinCrewEntity savedCabin;

        if (cabin.getLanguages() != null){

            CabinCrewEntity newCabin = new CabinCrewEntity(cabin);

            savedCabin = cabinCrewRepository.save(newCabin);

            List<AttendantLanguageEntity> savedCabinLanguageList = cabinLanguageRepository.saveAll(cabin.getLanguages());

            savedCabin.setLanguages(savedCabinLanguageList);
        }
        else {
            savedCabin = cabinCrewRepository.save(cabin);
        }

        return savedCabin;
    }

    @Override
    public CabinCrewEntity findAttendantById(int id){

        CabinCrewEntity cabinCrewEntity = cabinCrewRepository.findById(id).orElseThrow(()->new RuntimeException("Attendant with id " + id + " does not exist!"));
        return cabinCrewEntity;
    }



    @Override
    public boolean cabinCrewExistsById(int id){

      return cabinCrewRepository.existsById(id);
    }

    @Override
    public List<CabinCrewEntity> findAllCabinCrew(){

        return cabinCrewRepository.findAll();

    }

    @Override
    public CabinCrewEntity updateCabin(CabinCrewEntity cabin){

        if (!cabinCrewExistsById(cabin.getAttendantId())){

            throw new RuntimeException("Cabin crew member with id: " + cabin.getAttendantId() + " cannot be updated!");
        }

        CabinCrewEntity updatedCabin;
        if (cabin.getLanguages() != null){

            List<AttendantLanguageEntity> oldCabinCrewLanguageEntities = cabinLanguageRepository.findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(cabin.getAttendantId());

            cabinLanguageRepository.deleteAll(oldCabinCrewLanguageEntities);

            List<AttendantLanguageEntity> updatedAttendantLanguageEntitityList = cabinLanguageRepository.saveAll(cabin.getLanguages());

            CabinCrewEntity toBeUpdatedCabin = new CabinCrewEntity(cabin);

            updatedCabin = cabinCrewRepository.save(toBeUpdatedCabin);

            updatedCabin.setLanguages(updatedAttendantLanguageEntitityList);

        }
        else{

            updatedCabin = cabinCrewRepository.save(cabin);
        }

        return updatedCabin;
    }

    @Override
    public CabinCrewEntity deleteCabinById(int id){

        try{

            CabinCrewEntity cabinCrewEntity = findAttendantById(id);

            cabinCrewRepository.deleteById(id);

            return cabinCrewEntity;
        }
        catch (RuntimeException e){

            throw new RuntimeException("Cabin Crew with id " + id + " cannot be deleted!");

        }
    }

    @Override
    public boolean authenticate(LoginRequest loginRequest){

        Optional<CabinCrewEntity> cabinCrewEntity = cabinCrewRepository.findCabinCrewEntityByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        return cabinCrewEntity.isPresent();
    }

}
