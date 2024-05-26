package com.su.FlightScheduler.DTO.CabinCrewDTOs;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipeEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//DTO will be used for showing the general purpose of the cabin crew information
public class AttendantWithLanguagesDTO implements Serializable {

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

    public AttendantWithLanguagesDTO() {
    }

    public AttendantWithLanguagesDTO(CabinCrewEntity cabinCrewEntity) {

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
    }


    public AttendantWithLanguagesDTO(AttendantWithLanguagesAsStringDTO attendantWithLanguagesAsStringDTO){
        this.attendantId = attendantWithLanguagesAsStringDTO.getAttendantId();
        this.email = attendantWithLanguagesAsStringDTO.getEmail();
        this.password = attendantWithLanguagesAsStringDTO.getPassword();
        this.firstName = attendantWithLanguagesAsStringDTO.getFirstName();
        this.surname = attendantWithLanguagesAsStringDTO.getSurname();
        this.age = attendantWithLanguagesAsStringDTO.getAge();
        this.gender = attendantWithLanguagesAsStringDTO.getGender();
        this.nationality = attendantWithLanguagesAsStringDTO.getNationality();
        this.seniority = attendantWithLanguagesAsStringDTO.getSeniority();
        String[] languagesArray = attendantWithLanguagesAsStringDTO.getLanguages().split(",");
        this.languages = new ArrayList<>();
        for (String language : languagesArray) {
            this.languages.add(language.trim().toLowerCase());
        }
        this.recipes = null;
        if (attendantWithLanguagesAsStringDTO.getRecipes() != null || !attendantWithLanguagesAsStringDTO.getRecipes().isEmpty())
        {
            String[] recipesArray = attendantWithLanguagesAsStringDTO.getRecipes().split(",");
            this.recipes = new ArrayList<>();
            for(String recipe : recipesArray){
                this.recipes.add(recipe.toLowerCase());
            }
        }

    }


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

    @Override
    public String toString() {
        return "AttendantWithLanguagesDTO{" +
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
                '}';
    }
}
