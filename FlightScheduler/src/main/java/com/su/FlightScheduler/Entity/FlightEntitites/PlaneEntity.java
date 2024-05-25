package com.su.FlightScheduler.Entity.FlightEntitites;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "planes")
public class PlaneEntity implements Serializable {

    @Id
    @Column(name = "plane_id")
    private int planeId;

    @ManyToOne
    @JoinColumn(name = "vehicle_type", referencedColumnName = "vehicle_type")
    private VehicleTypeEntity vehicleType;

    // getters and setters
    public int getPlaneId() {
        return planeId;
    }

    public void setPlaneId(int planeId) {
        this.planeId = planeId;
    }

    public VehicleTypeEntity getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeEntity vehicleType) {
        this.vehicleType = vehicleType;
    }
}