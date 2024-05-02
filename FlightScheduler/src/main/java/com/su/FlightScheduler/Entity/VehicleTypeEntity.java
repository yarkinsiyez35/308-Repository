package com.su.FlightScheduler.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "vehicle_types")
public class VehicleTypeEntity implements Serializable {

    @Id
    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "business_capacity")
    private int businessCapacity;

    @Column(name = "economy_capacity")
    private int economyCapacity;

    @Column(name = "senior_pilot_capacity")
    private int seniorPilotCapacity;

    @Column(name = "junior_pilot_capacity")
    private int juniorPilotCapacity;

    @Column(name = "trainee_pilot_capacity")
    private int traineePilotCapacity;

    @Column(name = "senior_attendee_capacity")
    private int seniorAttendeeCapacity;

    @Column(name = "junior_attendee_capacity")
    private int juniorAttendeeCapacity;

    @Column(name = "chef_attendee_capacity")
    private int chefAttendeeCapacity;

    @Column(name = "seating_plan", columnDefinition = "TEXT")
    private String seatingPlan;

    // getters and setters
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getBusinessCapacity() {
        return businessCapacity;
    }

    public void setBusinessCapacity(int businessCapacity) {
        this.businessCapacity = businessCapacity;
    }

    public int getEconomyCapacity() {
        return economyCapacity;
    }

    public void setEconomyCapacity(int economyCapacity) {
        this.economyCapacity = economyCapacity;
    }

    public int getSeniorPilotCapacity() {
        return seniorPilotCapacity;
    }

    public void setSeniorPilotCapacity(int seniorPilotCapacity) {
        this.seniorPilotCapacity = seniorPilotCapacity;
    }

    public int getJuniorPilotCapacity() {
        return juniorPilotCapacity;
    }

    public void setJuniorPilotCapacity(int juniorPilotCapacity) {
        this.juniorPilotCapacity = juniorPilotCapacity;
    }

    public int getTraineePilotCapacity() {
        return traineePilotCapacity;
    }

    public void setTraineePilotCapacity(int traineePilotCapacity) {
        this.traineePilotCapacity = traineePilotCapacity;
    }

    public int getSeniorAttendeeCapacity() {
        return seniorAttendeeCapacity;
    }

    public void setSeniorAttendeeCapacity(int seniorAttendeeCapacity) {
        this.seniorAttendeeCapacity = seniorAttendeeCapacity;
    }

    public int getJuniorAttendeeCapacity() {
        return juniorAttendeeCapacity;
    }

    public void setJuniorAttendeeCapacity(int juniorAttendeeCapacity) {
        this.juniorAttendeeCapacity = juniorAttendeeCapacity;
    }

    public int getChefAttendeeCapacity() {
        return chefAttendeeCapacity;
    }

    public void setChefAttendeeCapacity(int chefAttendeeCapacity) {
        this.chefAttendeeCapacity = chefAttendeeCapacity;
    }

    public String getSeatingPlan() {
        return seatingPlan;
    }

    public void setSeatingPlan(String seatingPlan) {
        this.seatingPlan = seatingPlan;
    }
}