package com.su.FlightScheduler.Entity.CabinCrewEntites;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cabin_crew_assignments")
public class CabinCrewAssignmentsEntity implements Serializable {

    @EmbeddedId
    private CabinCrewAssignmentsPK cabinCrewAssignmentsPK;

    public CabinCrewAssignmentsEntity() {
    }

    public CabinCrewAssignmentsEntity(CabinCrewAssignmentsPK cabinCrewAssignmentsPK) {
        this.cabinCrewAssignmentsPK = cabinCrewAssignmentsPK;
    }

    public CabinCrewAssignmentsPK getCabinCrewAssignmentsPK() {
        return cabinCrewAssignmentsPK;
    }

    public void setCabinCrewAssignmentsPK(CabinCrewAssignmentsPK cabinCrewAssignmentsPK) {
        this.cabinCrewAssignmentsPK = cabinCrewAssignmentsPK;
    }
}
