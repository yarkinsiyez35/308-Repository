package com.su.FlightScheduler.DTO.PilotDTOs;


import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;

import java.util.ArrayList;
import java.util.List;

public class PilotFullDTO {
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
    private List<PilotAssignmentDTO> assignments;

    public PilotFullDTO() {
    }

    public PilotFullDTO(int pilotId, String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority, List<String> languages, List<PilotAssignmentDTO> assignments) {
        this.pilotId = pilotId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.allowedRange = allowedRange;
        this.nationality = nationality;
        this.seniority = seniority;
        this.languages = languages;
        this.assignments = assignments;
    }

    public PilotFullDTO(PilotEntity pilotEntity)
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
        this.assignments = new ArrayList<>();
        if (pilotEntity.getAssignments() != null)   //if there are assignments
        {
            List<PilotAssignmentEntity> pilotAssignmentEntities = pilotEntity.getAssignments();
            for (PilotAssignmentEntity pilotAssignmentEntity : pilotAssignmentEntities)
            {
                assignments.add(new PilotAssignmentDTO(pilotAssignmentEntity));
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

    public List<PilotAssignmentDTO> getAssignments() {
        return assignments;
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

    public void setAssignments(List<PilotAssignmentDTO> assignments) {
        this.assignments = assignments;
    }
}
