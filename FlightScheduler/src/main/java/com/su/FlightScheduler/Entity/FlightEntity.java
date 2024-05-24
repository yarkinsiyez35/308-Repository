package com.su.FlightScheduler.Entity;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights")
public class FlightEntity implements Serializable {

    @Id
    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "flight_info")
    private String flightInfo;

    @ManyToOne
    @JoinColumn(name = "source_airport_code", referencedColumnName = "airport_code")
    private AirportEntity sourceAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_code", referencedColumnName = "airport_code")
    private AirportEntity destinationAirport;

    @ManyToOne
    @JoinColumn(name = "plane_id", referencedColumnName = "plane_id")
    private PlaneEntity plane;

    @Column(name = "flight_range")
    private Integer flightRange;

    @Column(name = "departure_datetime")
    private LocalDateTime departureDateTime;

    @Column(name = "landing_datetime")
    private LocalDateTime landingDateTime;

    @Column(name = "shared_flight")
    private boolean sharedFlight;

    @ManyToOne
    @JoinColumn(name = "shared_flight_company", referencedColumnName = "company_name")
    private CompanyEntity sharedFlightCompany;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "admin_id")
    private AdminEntity admin;

    @Column(name = "standard_menu")
    private String standardMenu;


    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<PilotAssignmentEntity> pilotAssignmentEntityList;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List <CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList;


    public FlightEntity() {
    }

    public FlightEntity(String flightNumber, String flightInfo) {
        this.flightNumber = flightNumber;
        this.flightInfo = flightInfo;
    }

    public FlightEntity(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport) {
        this.flightNumber = flightNumber;
        this.flightInfo = flightInfo;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
    }

    public FlightEntity(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime, boolean sharedFlight, CompanyEntity sharedFlightCompany, AdminEntity admin, String standardMenu) {
        this.flightNumber = flightNumber;
        this.flightInfo = flightInfo;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.plane = plane;
        this.flightRange = flightRange;
        this.departureDateTime = departureDateTime;
        this.landingDateTime = landingDateTime;
        this.sharedFlight = sharedFlight;
        this.sharedFlightCompany = sharedFlightCompany;
        this.admin = admin;
        this.standardMenu = standardMenu;
    }

    // getters and setters
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(String flightInfo) {
        this.flightInfo = flightInfo;
    }

    public AirportEntity getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(AirportEntity sourceAirport) {
        this.sourceAirport = sourceAirport;
    }

    public AirportEntity getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(AirportEntity destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public PlaneEntity getPlane() {
        return plane;
    }

    public void setPlane(PlaneEntity plane) {
        this.plane = plane;
    }

    public Integer getFlightRange() {
        return flightRange;
    }

    public void setFlightRange(Integer flightRange) {
        this.flightRange = flightRange;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getLandingDateTime() {
        return landingDateTime;
    }

    public void setLandingDateTime(LocalDateTime landingDateTime) {
        this.landingDateTime = landingDateTime;
    }

    public boolean isSharedFlight() {
        return sharedFlight;
    }

    public void setSharedFlight(boolean sharedFlight) {
        this.sharedFlight = sharedFlight;
    }

    public CompanyEntity getSharedFlightCompany() {
        return sharedFlightCompany;
    }

    public void setSharedFlightCompany(CompanyEntity sharedFlightCompany) {
        this.sharedFlightCompany = sharedFlightCompany;
    }

    public AdminEntity getAdmin() {
        return admin;
    }

    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
    }

    public String getStandardMenu() {
        return standardMenu;
    }

    public void setStandardMenu(String standardMenu) {
        this.standardMenu = standardMenu;
    }


    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PassengerFlight> passengerFlights;

    public List<PassengerFlight> getPassengerFlights() {
        return passengerFlights;
    }

    public void setPassengerFlights(List<PassengerFlight> passengerFlights) {
        this.passengerFlights = passengerFlights;
    }


}