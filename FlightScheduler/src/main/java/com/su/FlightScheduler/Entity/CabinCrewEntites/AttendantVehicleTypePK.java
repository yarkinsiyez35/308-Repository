package com.su.FlightScheduler.Entity.CabinCrewEntites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class AttendantVehicleTypePK implements Serializable {

    @Column(name = "attendant_id", nullable = false)
    private int attendantId;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    public AttendantVehicleTypePK() {
    }

    public AttendantVehicleTypePK(int attendantId, String vehicleType) {
        this.attendantId = attendantId;
        this.vehicleType = vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendantVehicleTypePK that = (AttendantVehicleTypePK) o;
        return attendantId == that.attendantId && Objects.equals(vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {return Objects.hash(attendantId, vehicleType);}


    public int getAttendantId() {
        return attendantId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
