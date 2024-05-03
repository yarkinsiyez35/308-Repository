package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_recipes")
public class DishRecipeEntity {
    @EmbeddedId
    private DishRecipePK dishRecipePK;

    public DishRecipeEntity() {
    }

    public DishRecipeEntity(DishRecipePK dishRecipePK) {
        this.dishRecipePK = dishRecipePK;
    }

    public DishRecipePK getDishRecipePK() {
        return dishRecipePK;
    }

    public void setDishRecipePK(DishRecipePK dishRecipePK) {
        this.dishRecipePK = dishRecipePK;
    }
}
