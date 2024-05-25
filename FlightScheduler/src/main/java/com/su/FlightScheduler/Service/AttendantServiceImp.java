package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.LoginRequest;
import com.su.FlightScheduler.Entity.CabinCrewEntites.*;

import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinLanguageRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.DishRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

//TESTING: this service should be tested
@Service
@Transactional
public class AttendantServiceImp implements AttendantService {

    private final CabinCrewRepository cabinCrewRepository;
    private final CabinLanguageRepository cabinLanguageRepository;
    private final DishRecipeRepository dishRecipeRepository;

    @Autowired
    public AttendantServiceImp(CabinCrewRepository cabinCrewRepository, CabinLanguageRepository cabinLanguageRepository, DishRecipeRepository dishRecipeRepository) {
        this.cabinCrewRepository = cabinCrewRepository;
        this.cabinLanguageRepository = cabinLanguageRepository;
        this.dishRecipeRepository = dishRecipeRepository;
    }

    @Override
    public CabinCrewEntity saveCabin(CabinCrewEntity cabin){

        if (cabinCrewRepository.findCabinCrewEntityByEmail(cabin.getEmail()).isPresent()){

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

        if (cabin.getRecipes() != null)
        {
            List<DishRecipeEntity> savedDishRecipeList =  dishRecipeRepository.saveAll(cabin.getRecipes());
            savedCabin.setRecipes(savedDishRecipeList);
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
        if(cabinCrewRepository.findCabinCrewEntityByEmail(cabin.getEmail()).isPresent() && cabin.getAttendantId() != cabinCrewRepository.findCabinCrewEntityByEmail(cabin.getEmail()).get().getAttendantId()){

            throw new RuntimeException("Cabin crew member with id: " + cabin.getEmail() + " cannot be updated!");
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

        if (cabin.getRecipes() != null)
        {
            List<DishRecipeEntity> oldCabinCrewDishRecipeEntities = dishRecipeRepository.findDishRecipeEntitiesByDishRecipePK_AttendantId(cabin.getAttendantId());
            dishRecipeRepository.deleteAll(oldCabinCrewDishRecipeEntities);

            List<DishRecipeEntity> updatedCabinCrewDishRecipeEntitityList = dishRecipeRepository.saveAll(cabin.getRecipes());

            updatedCabin.setRecipes(updatedCabinCrewDishRecipeEntitityList);
        }

        return updatedCabin;
    }


    @Override
    public CabinCrewEntity saveAttendantWithoutId(CabinCrewEntity cabinCrewEntity) throws RuntimeException{
        if (cabinCrewRepository.findCabinCrewEntityByEmail(cabinCrewEntity.getEmail()).isPresent()){
            throw new RuntimeException("Cabin Crew member with email " + cabinCrewEntity.getEmail() + " cannot be created!");
        }

        CabinCrewEntity savedCabin;
        if (cabinCrewEntity.getLanguages() != null){

            CabinCrewEntity newCabin = new CabinCrewEntity(cabinCrewEntity);

            savedCabin = cabinCrewRepository.save(new CabinCrewEntity(cabinCrewEntity));

            List<AttendantLanguageEntity> languagesToBeSaved = cabinCrewEntity.getLanguages();

            for (AttendantLanguageEntity attendantLanguageEntity : languagesToBeSaved){

                AttendantLanguagePK attendantLanguagePK = new AttendantLanguagePK(savedCabin.getAttendantId(), attendantLanguageEntity.getAttendantLanguagePK().getLanguage());
                attendantLanguageEntity.setAttendantLanguagePK(attendantLanguagePK);
            }

            List<AttendantLanguageEntity> savedAttendantLanguageEntityList = cabinLanguageRepository.saveAll(cabinCrewEntity.getLanguages());
            savedCabin.setLanguages(savedAttendantLanguageEntityList);
        }
        else{
            savedCabin = cabinCrewRepository.save(cabinCrewEntity);
        }
        if (cabinCrewEntity.getRecipes() != null){

            List<DishRecipeEntity> recipesToBeSaved = cabinCrewEntity.getRecipes();

            for (DishRecipeEntity dishRecipeEntity : recipesToBeSaved){

                DishRecipePK dishRecipePK = new DishRecipePK(savedCabin.getAttendantId(), dishRecipeEntity.getDishRecipePK().getRecipe());
                dishRecipeEntity.setDishRecipePK(dishRecipePK);
            }

            List<DishRecipeEntity> savedDishRecipeEntityList = dishRecipeRepository.saveAll(cabinCrewEntity.getRecipes());
            savedCabin.setRecipes(savedDishRecipeEntityList);
        }
        return savedCabin;
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
