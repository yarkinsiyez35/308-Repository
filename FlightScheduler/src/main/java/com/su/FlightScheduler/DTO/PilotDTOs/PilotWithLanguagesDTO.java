package com.su.FlightScheduler.DTO.PilotDTOs;



import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//DTO will be used for showing general purpose pilot information
public class PilotWithLanguagesDTO implements Serializable {

    private int pilotId;
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private int age;
    private String gender;
    private int allowedRange;
    private String nationality;
    private String seniority;
    private List<String> languages;

    public PilotWithLanguagesDTO() {
    }

    public PilotWithLanguagesDTO(PilotEntity pilotEntity)
    {
        this.pilotId = pilotEntity.getPilotId();
        this.email = pilotEntity.getEmail();
        this.password = pilotEntity.getPassword();
        this.firstName = pilotEntity.getFirstName();
        this.surname = pilotEntity.getSurname();
        this.age = pilotEntity.getAge();
        this.gender = pilotEntity.getGender();
        this.allowedRange = pilotEntity.getAllowedRange();
        this.nationality = pilotEntity.getNationality();
        this.seniority = pilotEntity.getSeniority();
        this.languages = new ArrayList<>();
        if (pilotEntity.getLanguages() != null)  //if there are languages
        {
            List<PilotLanguageEntity> pilotLanguageEntities =  pilotEntity.getLanguages();
            for (PilotLanguageEntity pilotLanguageEntity :  pilotLanguageEntities)  //add each language
            {
                languages.add(pilotLanguageEntity.getPilotLanguagePK().getLanguage());
            }
        }
    }

    public int getPilotId() {
        return pilotId;
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

    public int getAllowedRange() {
        return allowedRange;
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


    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
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

    public void setAllowedRange(int allowedRange) {
        this.allowedRange = allowedRange;
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

    @Override
    public String toString() {
        return "PilotWithLanguagesDTO{" +
                "pilotId=" + pilotId +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", allowedRange=" + allowedRange +
                ", nationality='" + nationality + '\'' +
                ", seniority='" + seniority + '\'' +
                ", languages=" + languages +
                '}';
    }
}
