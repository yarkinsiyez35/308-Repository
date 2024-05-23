package com.su.FlightScheduler.DTO.FrontEndDTOs;


import com.su.FlightScheduler.Entity.FlightEntity;

import java.time.LocalDateTime;

public class FlightDataDTO {
    private String from;
    private String goTo;
    private String departureAirport;
    private String landingAirport;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private String planeType;
    private String airlineCompany;
    private String flightId;
    private String planeId;
    private String menu;

    // private String menu; //THIS WILL BE ADDED
    public FlightDataDTO() {
    }

    public FlightDataDTO(String from, String goTo, String departureAirport, String landingAirport, LocalDateTime departureTime, LocalDateTime landingTime, String planeType, String airlineCompany, String flightId, String planeId, String menu) {
        this.from = from;
        this.goTo = goTo;
        this.departureAirport = departureAirport;
        this.landingAirport = landingAirport;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.planeType = planeType;
        this.airlineCompany = airlineCompany;
        this.flightId = flightId;
        this.planeId = planeId;
        this.menu = menu;
    }

    public FlightDataDTO(FlightEntity flightEntity)
    {
        this.from = flightEntity.getSourceAirport().getCity();
        this.goTo = flightEntity.getDestinationAirport().getCity();
        this.departureAirport = flightEntity.getSourceAirport().getAirportName();
        this.landingAirport = flightEntity.getDestinationAirport().getAirportName();
        this.departureTime = flightEntity.getDepartureDateTime();
        this.landingTime = flightEntity.getLandingDateTime();
        this.planeType = flightEntity.getPlane().getVehicleType().getVehicleType();
        if (flightEntity.isSharedFlight())
        {
            this.airlineCompany = flightEntity.getSharedFlightCompany().getCompanyName();
        }
        else
        {
            this.airlineCompany = "No shared flight";
        }
        this.flightId = flightEntity.getFlightNumber();
        this.planeId = Integer.toString(flightEntity.getPlane().getPlaneId());
        this.menu = flightEntity.getStandardMenu();
    }

    public String getFrom() {
        return from;
    }

    public String getGoTo() {
        return goTo;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getLandingAirport() {
        return landingAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public String getPlaneType() {
        return planeType;
    }

    public String getAirlineCompany() {
        return airlineCompany;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getPlaneId() {
        return planeId;
    }

    public String getMenu() {
        return menu;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setGoTo(String goTo) {
        this.goTo = goTo;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setLandingAirport(String landingAirport) {
        this.landingAirport = landingAirport;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public void setPlaneType(String planeType) {
        this.planeType = planeType;
    }

    public void setAirlineCompany(String airlineCompany) {
        this.airlineCompany = airlineCompany;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
