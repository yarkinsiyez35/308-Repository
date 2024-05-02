package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_recipes")
public class DishRecipe {
    @EmbeddedId
    private DishRecipePK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id", insertable = false, updatable = false)
    private CabinCrewEntity cabinCrewEntity;

    // Constructors, getters and setters
    public DishRecipe() {
    }

    public DishRecipe(DishRecipePK id, CabinCrewEntity cabinCrewEntity) {
        this.id = id;
        this.cabinCrewEntity = cabinCrewEntity;
    }

    public DishRecipePK getId() {
        return id;
    }

    public void setId(DishRecipePK id) {
        this.id = id;
    }

    public CabinCrewEntity getCabinCrewEntity() {
        return cabinCrewEntity;
    }

    public void setCabinCrewEntity(CabinCrewEntity cabinCrewEntity) {
        this.cabinCrewEntity = cabinCrewEntity;
    }
}
