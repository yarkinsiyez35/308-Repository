package com.su.FlightScheduler.Model;



import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PilotWithLanguages implements Serializable {

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

    public PilotWithLanguages() {
    }

    public PilotWithLanguages(PilotEntity pilotEntity)
    {
        this.pilotId = pilotEntity.getPilotId();
        this.email = pilotEntity.getEmail();
        this.firstName = pilotEntity.getFirstName();
        this.surname = pilotEntity.getSurname();
        this.age = pilotEntity.getAge();
        this.gender = pilotEntity.getGender();
        this.allowedRange = pilotEntity.getAllowedRange();
        this.nationality = pilotEntity.getNationality();
        this.seniority = pilotEntity.getSeniority();
        this.languages = new ArrayList<>();
        if (!pilotEntity.getLanguages().isEmpty())  //if there are languages
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

    @Override
    public String toString() {
        return "PilotWithLanguages{" +
                "pilotId=" + pilotId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
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
