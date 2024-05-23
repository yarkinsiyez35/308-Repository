package com.su.FlightScheduler.DTO.CabinCrewDTOs;

import com.su.FlightScheduler.Entity.CabinCrewEntites.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CabinCrewFullDTO implements Serializable {

    private int attendantId;
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private int age;
    private String gender;
    private String nationality;
    private String seniority;
    private List<String> languages;
    private List<String> recipes;
    private List<String> vehicleTypes;
    private List<String> assignments;

    public CabinCrewFullDTO() {
    }

    public CabinCrewFullDTO(CabinCrewEntity cabinCrewEntity) {
        this.attendantId = cabinCrewEntity.getAttendantId();
        this.email = cabinCrewEntity.getEmail();
        this.password = cabinCrewEntity.getPassword();
        this.firstName = cabinCrewEntity.getFirstName();
        this.surname = cabinCrewEntity.getSurname();
        this.age = cabinCrewEntity.getAge();
        this.gender = cabinCrewEntity.getGender();
        this.nationality = cabinCrewEntity.getNationality();
        this.seniority = cabinCrewEntity.getSeniority();
        this.languages = new ArrayList<>();
        if (cabinCrewEntity.getLanguages() != null) {

            List<AttendantLanguageEntity> attendantLanguageEntities = cabinCrewEntity.getLanguages();
            for (AttendantLanguageEntity attendantLanguageEntity : attendantLanguageEntities) {
                languages.add(attendantLanguageEntity.getAttendantLanguagePK().getLanguage()); //where is the problem?
            }
        }

        this.recipes = new ArrayList<>();
        if(cabinCrewEntity.getRecipes() != null){

            List<DishRecipeEntity> dishRecipeEntities = cabinCrewEntity.getRecipes();
            for (DishRecipeEntity dishRecipeEntity : dishRecipeEntities){
                recipes.add(dishRecipeEntity.getDishRecipePK().getRecipe()); //where is the problem?
            }
        }
        this.vehicleTypes = new ArrayList<>();
        if(cabinCrewEntity.getVehicleTypes() != null){
            List<AttendantVehicleTypeEntity> attendantVehicleTypeEntities = cabinCrewEntity.getVehicleTypes();
            for (AttendantVehicleTypeEntity attendantVehicleTypeEntity : attendantVehicleTypeEntities){
                vehicleTypes.add(attendantVehicleTypeEntity.getAttendantVehicleTypePK().getVehicleType());
            }
        }

        this.assignments = new ArrayList<>();
        if(cabinCrewEntity.getAssignments() != null){
            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntities = cabinCrewEntity.getAssignments();
            for (CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity : cabinCrewAssignmentsEntities){
                assignments.add(cabinCrewAssignmentsEntity.getCabinCrewAssignmentsPK().getFlightNumber()); //I convert the flightNumber to string to add
            }
        }

    }

    //Getters
    public int getAttendantId() {
        return attendantId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getNationality() {
        return nationality;
    }

    public String getSeniority() {
        return seniority;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public List<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public List<String> getAssignments() {
        return assignments;
    }

    //Setters

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    public void setVehicleTypes(List<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "CabinCrewFullDTO{" +
                "attendantId=" + attendantId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", seniority='" + seniority + '\'' +
                ", languages=" + languages +
                ", recipes=" + recipes +
                ", vehicleTypes=" + vehicleTypes +
                ", assignments=" + assignments +
                '}';
    }
}
