package com.su.FlightScheduler.DTO.CabinCrewDTOs;

import java.io.Serializable;

public class AttendantWithLanguagesAsStringDTO implements Serializable{

    private int attendantId;
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private int age;
    private String gender;
    private String nationality;
    private String seniority;
    private String languages;
    private String recipes;

    public AttendantWithLanguagesAsStringDTO() {
    }

    public AttendantWithLanguagesAsStringDTO(int attendantId, String email, String password, String firstName, String surname, int age, String gender, String nationality, String seniority, String languages ,String recipes) {
        this.attendantId = attendantId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.seniority = seniority;
        this.languages = languages;
        this.recipes = recipes;
    }

    public AttendantWithLanguagesAsStringDTO(AttendantWithLanguagesDTO attendantWithLanguagesDTO) {

        this.attendantId = attendantWithLanguagesDTO.getAttendantId();
        this.email = attendantWithLanguagesDTO.getEmail();
        this.password = attendantWithLanguagesDTO.getPassword();
        this.firstName = attendantWithLanguagesDTO.getFirstName();
        this.surname = attendantWithLanguagesDTO.getSurname();
        this.age = attendantWithLanguagesDTO.getAge();
        this.gender = attendantWithLanguagesDTO.getGender();
        this.nationality = attendantWithLanguagesDTO.getNationality();
        this.seniority = attendantWithLanguagesDTO.getSeniority();

        StringBuilder sb = new StringBuilder();
        for (String attendantLanguage : attendantWithLanguagesDTO.getLanguages()) {
            if(sb.length() > 0) {
                sb.append(",");
            }
            sb.append(attendantLanguage);
        }
        this.languages = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        for (String dishRecipe : attendantWithLanguagesDTO.getRecipes()) {
            if(sb2.length() > 0) {
                sb2.append(",");
            }
            sb2.append(dishRecipe);
        }
        this.recipes = sb2.toString();
    }

    //getters
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

    public String getLanguages() {
        return languages;
    }

    public String getRecipes() {
        return recipes;
    }

    //setters


    public void setRecipes(String recipes) {
        this.recipes = recipes;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }
}
