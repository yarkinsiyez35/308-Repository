package com.su.FlightScheduler.DTO;

import com.su.FlightScheduler.Entity.AirportEntity;
import com.su.FlightScheduler.Entity.FlightEntity;


import java.time.LocalDateTime;
import java.util.Objects;

public class forPassenger_FlightDTO {

    private String flightNumber;
    private String flightInfo;
    private AirportEntity sourceAirport;
    private AirportEntity destinationAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime landingDateTime;
    private String standardMenu;

    public forPassenger_FlightDTO() {}

    public forPassenger_FlightDTO(FlightEntity flightEntity) {
        this.flightNumber = flightEntity.getFlightNumber();
        this.flightInfo = flightEntity.getFlightInfo();
        this.sourceAirport = flightEntity.getSourceAirport();
        this.destinationAirport = flightEntity.getDestinationAirport();
        this.departureDateTime = flightEntity.getDepartureDateTime();
        this.landingDateTime = flightEntity.getLandingDateTime();
        this.standardMenu = flightEntity.getStandardMenu();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getStandardMenu() {
        return standardMenu;
    }

    public void setStandardMenu(String standardMenu) {
        this.standardMenu = standardMenu;
    }

    public LocalDateTime getLandingDateTime() {
        return landingDateTime;
    }

    public void setLandingDateTime(LocalDateTime landingDateTime) {
        this.landingDateTime = landingDateTime;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public AirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(AirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public AirportEntity getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(AirportEntity sourceAirport) {
        this.sourceAirport = sourceAirport;
    }

    public String getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(String flightInfo) {
        this.flightInfo = flightInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        forPassenger_FlightDTO that = (forPassenger_FlightDTO) o;
        return Objects.equals(getFlightNumber(), that.getFlightNumber()) && Objects.equals(getFlightInfo(), that.getFlightInfo()) && Objects.equals(getSourceAirport(), that.getSourceAirport()) && Objects.equals(getDestinationAirport(), that.getDestinationAirport()) && Objects.equals(getDepartureDateTime(), that.getDepartureDateTime()) && Objects.equals(getLandingDateTime(), that.getLandingDateTime()) && Objects.equals(getStandardMenu(), that.getStandardMenu());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlightNumber(), getFlightInfo(), getSourceAirport(), getDestinationAirport(), getDepartureDateTime(), getLandingDateTime(), getStandardMenu());
    }
}
