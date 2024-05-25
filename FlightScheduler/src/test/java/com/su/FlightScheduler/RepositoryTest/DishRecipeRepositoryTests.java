package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipeEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipePK;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.DishRecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DishRecipeRepositoryTests {

    @Autowired
    private DishRecipeRepository dishRecipeRepository;

    @Test
    public void DishRecipeRepository_FindDishRecipeEntitiesByDishRecipePK_AttendantId() {
        DishRecipePK dishRecipePK = new DishRecipePK(1, "Pasta");

        DishRecipeEntity dishRecipeEntity = new DishRecipeEntity(dishRecipePK);

        dishRecipeRepository.save(dishRecipeEntity);

        List<DishRecipeEntity> dishRecipes = dishRecipeRepository.findDishRecipeEntitiesByDishRecipePK_AttendantId(1);

        Assertions.assertThat(dishRecipes).isNotEmpty();
        Assertions.assertThat(dishRecipes.size()).isEqualTo(1);
        Assertions.assertThat(dishRecipes.get(0).getDishRecipePK().getAttendantId()).isEqualTo(1);
        Assertions.assertThat(dishRecipes.get(0).getDishRecipePK().getRecipe()).isEqualTo("Pasta");
    }
}
