package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/*
CREATE TABLE passengers(
    passenger_id INT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(8) NOT NULL,
    nationality VARCHAR(50) NOT NULL
);
 */

@Entity
@Table(name = "passengers")
public class PassengerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private int passengerId;

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

    public PassengerEntity() {
    }

    public PassengerEntity(String email, String password,
                           String firstName, String surname, int age, String gender, String nationality) {

        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
    }

    public PassengerEntity(int passengerId, String email, String password,
                           String firstName, String surname, int age, String gender, String nationality) {

        this.passengerId = passengerId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
    }

    public PassengerEntity(PassengerEntity passengerEntity) {
        this.passengerId = passengerEntity.getPassengerId();
        this.email = passengerEntity.getEmail();
        this.password = passengerEntity.getPassword();
        this.firstName = passengerEntity.getFirstName();
        this.surname = passengerEntity.getSurname();
        this.age = passengerEntity.getAge();
        this.gender = passengerEntity.getGender();
        this.nationality = passengerEntity.getNationality();
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerEntity that = (PassengerEntity) o;
        return getPassengerId() == that.getPassengerId() && getAge() == that.getAge()
                && Objects.equals(getEmail(), that.getEmail())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(getFirstName(), that.getFirstName())
                && Objects.equals(getSurname(), that.getSurname())
                && Objects.equals(getGender(), that.getGender())
                && Objects.equals(getNationality(), that.getNationality());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getPassengerId(), getEmail(), getPassword(), getFirstName(), getSurname(), getAge(), getGender(), getNationality());
    }

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PassengerFlight> passengerFlights;

    public List<PassengerFlight> getPassengerFlights() {
        return passengerFlights;
    }

    public void setPassengerFlights(List<PassengerFlight> passengerFlights) {
        this.passengerFlights = passengerFlights;
    }


}
