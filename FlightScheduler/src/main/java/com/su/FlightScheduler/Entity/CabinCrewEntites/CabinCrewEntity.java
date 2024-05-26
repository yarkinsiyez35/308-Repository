package com.su.FlightScheduler.Entity.CabinCrewEntites;
import com.su.FlightScheduler.DTO.CabinCrewDTOs.AttendantWithLanguagesDTO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crew_members")
public class CabinCrewEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //this needs to be added for the sql to automatically generate id
    @Column(name = "attendant_id")
    private int attendantId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "seniority", nullable = false)
    private String seniority;


    @OneToMany(mappedBy = "attendantLanguagePK.attendantId", cascade = CascadeType.REMOVE)
    private List<AttendantLanguageEntity> languages;

    @OneToMany(mappedBy = "dishRecipePK.attendantId", cascade = CascadeType.REMOVE)
    private List<DishRecipeEntity> recipes;


    //attendant vehicle type ı aynı olcak
    @OneToMany(mappedBy = "attendantVehicleTypePK.attendantId", cascade = CascadeType.PERSIST) //ata added here
    private List<AttendantVehicleTypeEntity> vehicleTypes;

    @OneToMany(mappedBy = "cabinCrewAssignmentsPK.attendantId", cascade = CascadeType.PERSIST)
    private List<CabinCrewAssignmentsEntity> assignments;


    // Constructor
    public CabinCrewEntity() {
    }

    public CabinCrewEntity(int attendantId, String email, String password, String firstName, String surname, int age, String gender, String nationality, String seniority) {
        this.attendantId = attendantId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.seniority = seniority;
        //this.languages = languages;
        //this.recipes = recipes;
        //this.assignments = assignments; //ata added
        //this.vehicleTypes = vehicleTypes; //ata added
    }

    public CabinCrewEntity(CabinCrewEntity cabin)
    {
        this.attendantId = cabin.getAttendantId();
        this.email = cabin.getEmail();
        this.password = cabin.getPassword();;
        this.firstName = cabin.getFirstName();
        this.surname = cabin.getSurname();
        this.age = cabin.getAge();
        this.gender = cabin.getGender();
        this.nationality = cabin.getNationality();
        this.seniority = cabin.getSeniority();
    }

    public CabinCrewEntity(AttendantWithLanguagesDTO attendantWithLanguagesDTO) { // ata created this for controller error
    }

    public CabinCrewEntity(AttendantWithLanguagesDTO attendantWithLanguagesDTO, boolean idIsPresent){

        if (idIsPresent){
            this.attendantId = attendantWithLanguagesDTO.getAttendantId();
        }
        this.email = attendantWithLanguagesDTO.getEmail();
        this.password = attendantWithLanguagesDTO.getPassword();
        this.firstName = attendantWithLanguagesDTO.getFirstName();
        this.surname = attendantWithLanguagesDTO.getSurname();
        this.age = attendantWithLanguagesDTO.getAge();
        this.gender = attendantWithLanguagesDTO.getGender();
        this.nationality = attendantWithLanguagesDTO.getNationality();
        this.seniority = attendantWithLanguagesDTO.getSeniority();
        List<AttendantLanguageEntity> languageEntities = new ArrayList<>();
        for (String language : attendantWithLanguagesDTO.getLanguages()){

            AttendantLanguagePK attendantLanguagePK = new AttendantLanguagePK( this.attendantId, language);
            AttendantLanguageEntity attendantLanguageEntity = new AttendantLanguageEntity(attendantLanguagePK);
            languageEntities.add(attendantLanguageEntity);
        }
        this.languages = languageEntities;

        List<DishRecipeEntity> dishRecipeEntityList = new ArrayList<>();
        for (String recipe: attendantWithLanguagesDTO.getRecipes())
        {
            DishRecipePK dishRecipePK = new DishRecipePK(this.attendantId, recipe);
            DishRecipeEntity dishRecipeEntity = new DishRecipeEntity(dishRecipePK);
            dishRecipeEntityList.add(dishRecipeEntity);
        }
        this.recipes = dishRecipeEntityList;
    }

    // Getters
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

    public List<AttendantLanguageEntity> getLanguages() {
        return languages;
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

    public List<DishRecipeEntity> getRecipes() {
        return recipes;
    }

    public List<AttendantVehicleTypeEntity> getVehicleTypes() { //ata added
        return vehicleTypes;
    }

    public List<CabinCrewAssignmentsEntity> getAssignments() { //ata added
        return assignments;
    }

    // Setters
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
    public void setLanguages(List<AttendantLanguageEntity> languages) {
        this.languages = languages;
    }

    public void setRecipes(List<DishRecipeEntity> recipes) {
        this.recipes = recipes;
    }

    public void setVehicleTypes(List<AttendantVehicleTypeEntity> vehicleTypes) { //ata added
        this.vehicleTypes = vehicleTypes;
    }

    public void setAssignments(List<CabinCrewAssignmentsEntity> assignments) { //ata added
        this.assignments = assignments;
    }
}
