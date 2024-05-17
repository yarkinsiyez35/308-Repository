package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.AdminRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FlightServiceImp implements FlightService {

    private final FlightRepository flightRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository, AdminRepository adminRepository) {
        this.flightRepository = flightRepository;
        this.adminRepository = adminRepository;
    }

    private void verifyAdmin(AdminEntity admin) {
        if (!adminRepository.existsById(admin.getAdminId())) {
            throw new RuntimeException("Admin not found");
        }
    }

    @Override
    public FlightEntity saveFlight(FlightEntity flight, AdminEntity admin) {
        verifyAdmin(admin);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity createFlight(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime, boolean sharedFlight, CompanyEntity sharedFlightCompany, AdminEntity admin, String standardMenu) {
        verifyAdmin(admin);
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo, sourceAirport, destinationAirport, plane, flightRange, departureDateTime, landingDateTime, sharedFlight, sharedFlightCompany, admin, standardMenu);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity createFlight(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo, sourceAirport, destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity createFlight(String flightNumber, String flightInfo, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo);
        return flightRepository.save(flight);
    }

    @Override
    public Optional<FlightEntity> findFlightByNumber(String flightNumber, AdminEntity admin) {
        verifyAdmin(admin);
        return flightRepository.findById(flightNumber);
    }

    @Override
    public List<FlightEntity> findAllFlights(AdminEntity admin) {
        verifyAdmin(admin);
        return flightRepository.findAll();
    }

    @Override
    public FlightEntity updateFlight(FlightEntity flight, AdminEntity admin) {
        verifyAdmin(admin);
        return flightRepository.save(flight);
    }

    @Override
    public void deleteFlightByNumber(String flightNumber, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flightRepository.delete(flight);
    }


    @Override
    public FlightEntity updateFlightInfo(String flightNumber, String flightInfo, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setFlightInfo(flightInfo);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setSourceAirport(sourceAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updatePlane(String flightNumber, PlaneEntity plane, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setPlane(plane);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateFlightRange(String flightNumber, Integer flightRange, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setFlightRange(flightRange);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setDepartureDateTime(departureDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setLandingDateTime(landingDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlight(String flightNumber, boolean sharedFlight, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setSharedFlight(sharedFlight);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlightCompany(String flightNumber, CompanyEntity sharedFlightCompany, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }

    /*@Override
    public FlightEntity updateAdmin(String flightNumber, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setAdmin(admin);
        return flightRepository.save(flight);
    }*/

    @Override
    public FlightEntity updateStandardMenu(String flightNumber, String standardMenu, AdminEntity admin) {
        verifyAdmin(admin);
        FlightEntity flight = flightRepository.findById(flightNumber).orElseThrow();
        flight.setStandardMenu(standardMenu);
        return flightRepository.save(flight);
    }
}