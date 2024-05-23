package com.su.FlightScheduler.DTO.FrontEndDTOs;

import com.su.FlightScheduler.Entity.PassengerFlight;
=======
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;

import java.util.ArrayList;
import java.util.List;

public class UserDataDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String id;
    private Integer age;
    private String gender;
    private String nationality;
    private String userType;    //PilotCrew , CabinCrew, Admin, Passenger
    private List<UserFlightDataDTO> flights;

    public UserDataDTO() {
    }

    public UserDataDTO(String email, String password, String name, String surname, String id, Integer age, String gender, String nationality, String userType, List<UserFlightDataDTO> flights) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.userType = userType;
        this.flights = flights;
    }

    public UserDataDTO(PilotAssignmentEntity pilotAssignmentEntity) //this will be called when there is one assignment
    {
        this.email = pilotAssignmentEntity.getPilot().getEmail();
        this.password = pilotAssignmentEntity.getPilot().getPassword();
        this.name = pilotAssignmentEntity.getPilot().getFirstName();
        this.surname = pilotAssignmentEntity.getPilot().getSurname();
        this.id = Integer.toString(pilotAssignmentEntity.getPilot().getPilotId());
        this.age = pilotAssignmentEntity.getPilot().getAge();
        this.gender = pilotAssignmentEntity.getPilot().getGender();
        this.nationality = pilotAssignmentEntity.getPilot().getNationality();
        this.userType = "PilotCrew";
        this.flights = new ArrayList<>();
        this.flights.add(new UserFlightDataDTO(pilotAssignmentEntity.getFlight(), pilotAssignmentEntity));
    }

    public UserDataDTO(List<PilotAssignmentEntity> pilotAssignmentEntityList)   //this will be called when there are more than one assignment
    {
        PilotAssignmentEntity pilotAssignmentEntity = pilotAssignmentEntityList.get(0);
        this.email = pilotAssignmentEntity.getPilot().getEmail();
        this.password = pilotAssignmentEntity.getPilot().getPassword();
        this.name = pilotAssignmentEntity.getPilot().getFirstName();
        this.surname = pilotAssignmentEntity.getPilot().getSurname();
        this.id = Integer.toString(pilotAssignmentEntity.getPilot().getPilotId());
        this.age = pilotAssignmentEntity.getPilot().getAge();
        this.gender = pilotAssignmentEntity.getPilot().getGender();
        this.nationality = pilotAssignmentEntity.getPilot().getNationality();
        this.userType = "PilotCrew";
        this.flights = new ArrayList<>();
        for (PilotAssignmentEntity pilotAssignment : pilotAssignmentEntityList)
        {
            this.flights.add(new UserFlightDataDTO(pilotAssignment.getFlight(), pilotAssignment));
        }
    }

    public UserDataDTO(PassengerFlight passengerFlight) //this will be called when there is one assignment
    {
        this.email = passengerFlight.getPassenger().getEmail();
        this.password = passengerFlight.getPassenger().getPassword();
        this.name = passengerFlight.getPassenger().getFirstName();
        this.surname = passengerFlight.getPassenger().getSurname();
        this.id = Integer.toString(passengerFlight.getPassenger().getPassengerId());
        this.age = passengerFlight.getPassenger().getAge();
        this.gender = passengerFlight.getPassenger().getGender();
        this.nationality = passengerFlight.getPassenger().getNationality();
        this.userType = "Passenger";
        this.flights = new ArrayList<>();
        this.flights.add(new UserFlightDataDTO(passengerFlight.getFlight(), passengerFlight));
    }

=======
    public UserDataDTO(CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity){

        this.email = cabinCrewAssignmentsEntity.getCabinCrew().getEmail();
        this.password = cabinCrewAssignmentsEntity.getCabinCrew().getPassword();
        this.name = cabinCrewAssignmentsEntity.getCabinCrew().getFirstName();
        this.surname = cabinCrewAssignmentsEntity.getCabinCrew().getSurname();
        this.id = Integer.toString(cabinCrewAssignmentsEntity.getCabinCrew().getAttendantId());
        this.gender = cabinCrewAssignmentsEntity.getCabinCrew().getGender();
        this.nationality = cabinCrewAssignmentsEntity.getCabinCrew().getNationality();
        this.userType = "CabinCrew";
        this.flights = new ArrayList<>();
        this.flights.add(new UserFlightDataDTO(cabinCrewAssignmentsEntity.getFlight(), cabinCrewAssignmentsEntity));
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getNationality() {
        return nationality;
    }

    public String getUserType() {
        return userType;
    }

    public List<UserFlightDataDTO> getFlights() {
        return flights;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setFlights(List<UserFlightDataDTO> flights) {
        this.flights = flights;
    }
}
