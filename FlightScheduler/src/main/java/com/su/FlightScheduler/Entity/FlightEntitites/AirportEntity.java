package com.su.FlightScheduler.Entity.FlightEntitites;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "airports")
public class AirportEntity implements Serializable {

    @Id
    @Column(name = "airport_code")
    private String airportCode;


    @ManyToOne
    @JoinColumn(name = "city", referencedColumnName = "city_name")
    private CityEntity city;


    @Column(name = "country")
    private String country;

    @Column(name = "airport_name")
    private String airportName;


    public AirportEntity() {
    }

    public AirportEntity(String airportCode, CityEntity city, String country, String airportName) {
        this.airportCode = airportCode;
        this.city = city;
        this.country = country;
        this.airportName = airportName;
    }

    // getters and setters
    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }
}