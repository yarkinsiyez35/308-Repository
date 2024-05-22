package com.su.FlightScheduler.DTO.PilotDTOs;

import java.util.List;

//this DTO will be used for creating, updating Pilot
public class PilotWithLanguagesAsStringDTO {
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
    private String languages;

    public PilotWithLanguagesAsStringDTO() {
    }

    public PilotWithLanguagesAsStringDTO(int pilotId, String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority, String languages) {
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
    }

    public PilotWithLanguagesAsStringDTO(PilotWithLanguagesDTO pilotWithLanguagesDTO)
    {
        this.pilotId = pilotWithLanguagesDTO.getPilotId();
        this.email = pilotWithLanguagesDTO.getEmail();
        this.password = pilotWithLanguagesDTO.getPassword();
        this.firstName = pilotWithLanguagesDTO.getFirstName();
        this.surname = pilotWithLanguagesDTO.getSurname();
        this.age = pilotWithLanguagesDTO.getAge();
        this.gender = pilotWithLanguagesDTO.getGender();
        this.allowedRange = pilotWithLanguagesDTO.getAllowedRange();
        this.nationality = pilotWithLanguagesDTO.getNationality();
        this.seniority = pilotWithLanguagesDTO.getSeniority();

        StringBuilder sb = new StringBuilder();
        for (String pilotLanguage : pilotWithLanguagesDTO.getLanguages()) {
            if (sb.length() > 0) {
                sb.append(","); // Append comma as separator
            }
            sb.append(pilotLanguage);
        }
        this.languages = sb.toString();
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

    public String getLanguages() {
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

    public void setLanguages(String languages) {
        this.languages = languages;
    }
}
