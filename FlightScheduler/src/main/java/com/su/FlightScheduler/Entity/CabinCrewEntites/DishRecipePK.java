package com.su.FlightScheduler.Entity.CabinCrewEntites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DishRecipePK implements Serializable {
    @Column(name = "attendant_id")
    private int attendantId;

    @Column(name = "recipe")
    private String recipe;

    public DishRecipePK() {
    }

    public DishRecipePK(int attendantId, String recipe) {
        this.attendantId = attendantId;
        this.recipe = recipe;
    }

    public int getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishRecipePK that = (DishRecipePK) o;
        return attendantId == that.attendantId && Objects.equals(recipe, that.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendantId, recipe);
    }
}
