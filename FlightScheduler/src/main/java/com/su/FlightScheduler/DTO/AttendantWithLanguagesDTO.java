package com.su.FlightScheduler.DTO;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.Entity.AttendantLanguageEntity;

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

    




}
