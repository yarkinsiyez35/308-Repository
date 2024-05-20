package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "attendant_vehicle_types")
public class AttendantVehicleTypeEntity implements Serializable {

    @EmbeddedId
    private AttendantVehicleTypePK attendantVehicleTypePK;

    public AttendantVehicleTypeEntity() {
    }

    public AttendantVehicleTypeEntity(AttendantVehicleTypePK attendantVehicleTypePK) {
        this.attendantVehicleTypePK = attendantVehicleTypePK;
    }

    public AttendantVehicleTypePK getAttendantVehicleTypePK() {
        return attendantVehicleTypePK;
    }

    public void setAttendantVehicleTypePK(AttendantVehicleTypePK attendantVehicleTypePK) {
        this.attendantVehicleTypePK = attendantVehicleTypePK;
    }
}
