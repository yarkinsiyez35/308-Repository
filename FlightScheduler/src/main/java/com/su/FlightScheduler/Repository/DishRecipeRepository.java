package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.DishRecipeEntity;
import com.su.FlightScheduler.Entity.DishRecipePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface DishRecipeRepository extends JpaRepository<DishRecipeEntity, DishRecipePK> {

    public List<DishRecipeEntity> findDishRecipeEntitiesByDishRecipePK_AttendantId(int attendantId);
}
