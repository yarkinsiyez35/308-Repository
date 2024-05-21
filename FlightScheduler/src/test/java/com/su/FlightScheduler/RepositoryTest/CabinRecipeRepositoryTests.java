package com.su.FlightScheduler.RepositoryTest;


import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.CabinLanguageRepository;
import com.su.FlightScheduler.Repository.DishRecipeRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedTransferQueue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinRecipeRepositoryTests {
    @Autowired
    private CabinCrewRepository cabinCrewRepository;

    @Autowired
    private DishRecipeRepository dishRecipeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void CabinRecipeRepository_SaveRecipe() {
        // Save a CabinCrewEntity first
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Chef");
        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);
        // Create and save a DishRecipeEntity
        DishRecipeEntity dishRecipeEntity = new DishRecipeEntity();
        dishRecipeEntity.setDishRecipePK(new DishRecipePK(savedCabinCrewEntity.getAttendantId(), "Meatball"));
        DishRecipeEntity savedRecipeEntity = dishRecipeRepository.save(dishRecipeEntity);

        assertThat(savedRecipeEntity).isNotNull();
        assertThat(savedRecipeEntity.getDishRecipePK().getAttendantId()).isEqualTo(savedCabinCrewEntity.getAttendantId());
        assertThat(savedRecipeEntity.getDishRecipePK().getRecipe()).isEqualTo("Meatball");
    }

    @Test
    public void CabinRecipeRepository_SaveRecipes() {
        // Save a CabinCrewEntity first
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Chef");
        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);

        // Save multiple ChefRecipeEntities
        DishRecipeEntity recipeEntity1 = new DishRecipeEntity(new DishRecipePK(1, "Meatball"));
        DishRecipeEntity recipeEntity2 = new DishRecipeEntity(new DishRecipePK(1, "Rice"));

        List<DishRecipeEntity> recipeEntityList = new ArrayList<>();
        recipeEntityList.add(recipeEntity1);
        recipeEntityList.add(recipeEntity2);

        List<DishRecipeEntity> savedRecipeEntityList = dishRecipeRepository.saveAll(recipeEntityList);

        assertThat(savedRecipeEntityList).isNotNull();
        assertThat(savedRecipeEntityList.size()).isEqualTo(2);
    }


    @Test
    public void CabinRecipeRepository_FindRecipesById() {
        // Save a CabinCrewEntity first and then recipes
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Chef");
        cabinCrewRepository.save(cabinCrewEntity);

        // Create and save multiple DishRecipeEntities
        DishRecipeEntity recipeEntity1 = new DishRecipeEntity();
        recipeEntity1.setDishRecipePK(new DishRecipePK(cabinCrewEntity.getAttendantId(), "Meatball"));
        DishRecipeEntity recipeEntity2 = new DishRecipeEntity();
        recipeEntity2.setDishRecipePK(new DishRecipePK(cabinCrewEntity.getAttendantId(), "Rice"));
        dishRecipeRepository.saveAll(List.of(recipeEntity1, recipeEntity2));

        // Retrieve recipes by attendant ID
        List<DishRecipeEntity> foundRecipes = dishRecipeRepository.findDishRecipeEntitiesByDishRecipePK_AttendantId(cabinCrewEntity.getAttendantId());

        assertThat(foundRecipes).isNotNull();
        assertThat(foundRecipes.size()).isEqualTo(2);
    }

    @Test
    public void CabinRecipesRepository_DeleteCabinCrewById() {
        // Save a CabinCrewEntity and a recipe
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Chef");
        cabinCrewRepository.save(cabinCrewEntity);

        DishRecipeEntity recipeEntity1 = new DishRecipeEntity();
        recipeEntity1.setDishRecipePK(new DishRecipePK(cabinCrewEntity.getAttendantId(), "Meatball"));


        entityManager.clear();

        // Delete the CabinCrewEntity
        cabinCrewRepository.deleteById(cabinCrewEntity.getAttendantId());

        // Retrieve all recipes to check if they have been deleted
        List<DishRecipeEntity> remainingRecipes = dishRecipeRepository.findAll();
        assertThat(remainingRecipes).isEmpty();
    }

    }


