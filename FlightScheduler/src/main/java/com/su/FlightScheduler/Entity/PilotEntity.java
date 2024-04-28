package com.su.FlightScheduler.Entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pilots")
public class PilotEntity {

    @Id
    @Column(name = "pilot_id")
    private int pilotId;

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

    @Column(name = "allowed_range", nullable = false)
    private int allowedRange;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "seniority", nullable = false)
    private String seniority;

    @ElementCollection
    @CollectionTable(name = "pilot_languages", joinColumns = @JoinColumn(name = "pilot_id"))
    @Column(name = "language")
    private List<String> languages;

    public PilotEntity() {}

    public PilotEntity(int pilotId, String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority) {
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
}
