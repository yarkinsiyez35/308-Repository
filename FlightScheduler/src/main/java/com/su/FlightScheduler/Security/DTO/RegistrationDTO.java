package com.su.FlightScheduler.Security.DTO;

public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
    private String gender;
    private String nationality;

    public RegistrationDTO(){
        super();
    }

    public RegistrationDTO(String firstName, String lastName, String username, String password, int age, String gender, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password;
    }
}
