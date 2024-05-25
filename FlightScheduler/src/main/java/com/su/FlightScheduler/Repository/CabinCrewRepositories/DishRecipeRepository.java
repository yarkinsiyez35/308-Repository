package com.su.FlightScheduler.Repository.CabinCrewRepositories;

import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipeEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//TESTING: this one function should be tested
public interface DishRecipeRepository extends JpaRepository<DishRecipeEntity, DishRecipePK> {

    public List<DishRecipeEntity> findDishRecipeEntitiesByDishRecipePK_AttendantId(int attendantId);
}
