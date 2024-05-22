package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.AdminRepository;
import com.su.FlightScheduler.Repository.AirportRepository;
import com.su.FlightScheduler.Repository.VehicleTypeRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PlaneRepository;
import com.su.FlightScheduler.Repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class FlightServiceImp implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;
    private final CompanyRepository companyRepository;

    private final VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository, AirportRepository airportRepository,
                            PlaneRepository planeRepository, CompanyRepository companyRepository,
                            VehicleTypeRepository vehicleTypeRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.planeRepository = planeRepository;
        this.companyRepository = companyRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }


    @Override
    public FlightEntity saveFlightObj(FlightEntity flight) {
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity createFlightFilled(String flightNumber, String flightInfo, AirportEntity sourceAirport, AirportEntity destinationAirport, PlaneEntity plane, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime, boolean sharedFlight, CompanyEntity sharedFlightCompany, AdminEntity admin, String standardMenu) {
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo, sourceAirport, destinationAirport, plane, flightRange, departureDateTime, landingDateTime, sharedFlight, sharedFlightCompany, admin, standardMenu);
        return flightRepository.save(flight);
    }

    // Multi-Layered Create Method
    @Override
    public FlightEntity createFlight(String flightNumber, String flightInfo, AdminEntity admin) {
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo);
        flight.setAdmin(admin);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams1(String flightNumber, PlaneEntity plane, AirportEntity sourceAirport, AirportEntity destinationAirport) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setPlane(plane);
        flight.setSourceAirport(sourceAirport);
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams2(String flightNumber, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setFlightRange(flightRange);
        flight.setDepartureDateTime(departureDateTime);
        flight.setLandingDateTime(landingDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams3(String flightNumber, boolean sharedFlight, CompanyEntity sharedFlightCompany) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setSharedFlight(sharedFlight);
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }

    // End of Multi-Layered Create Method

    @Override
    public Optional<FlightEntity> findFlightByNumber(String flightNumber) {
        Optional<FlightEntity> flight = flightRepository.findById(flightNumber);
        if(flight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        return flight;
    }

    @Override
    public List<FlightEntity> findAllFlights() {
        return flightRepository.findAll();
    }

    // This method will not be used, but it is here to show how to update an object
    @Override
    public FlightEntity updateFlight(FlightEntity flight) {
        return flightRepository.save(flight);
    }

    @Override
    public void deleteFlightByNumber(String flightNumber) {
        Optional<FlightEntity> flight = flightRepository.findById(flightNumber);
        if(flight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.deleteById(flightNumber);
    }


    @Override
    public FlightEntity updateFlightInfo(String flightNumber, String flightInfo) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setFlightInfo(flightInfo);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setSourceAirport(sourceAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updatePlane(String flightNumber, PlaneEntity plane) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setPlane(plane);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateFlightRange(String flightNumber, Integer flightRange) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setFlightRange(flightRange);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setDepartureDateTime(departureDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setLandingDateTime(landingDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlight(String flightNumber, boolean sharedFlight) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setSharedFlight(sharedFlight);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlightCompany(String flightNumber, CompanyEntity sharedFlightCompany) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateStandardMenu(String flightNumber, String standardMenu) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        flight.setStandardMenu(standardMenu);
        return flightRepository.save(flight);
    }


    // Simple getters for the entities of the FlightEntity
    @Override
    public AirportEntity getSourceAirport(String flightNumber) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        return flight.getSourceAirport();
    }

    @Override
    public AirportEntity getDestinationAirport(String flightNumber) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new EntityNotFoundException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        return flight.getDestinationAirport();
    }

    @Override
    public PlaneEntity getPlane(String flightNumber) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        return flight.getPlane();
    }

    @Override
    public CompanyEntity getCompany(String flightNumber) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        return flight.getSharedFlightCompany();
    }

    @Override
    public LocalDateTime getDateTime(String flightNumber) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isEmpty()) {
            throw new RuntimeException("Flight not found");
        }
        FlightEntity flight = optionalFlight.get();
        return flight.getDepartureDateTime();
    }

    // End of Simple getters for the entities of the FlightEntity




    // Getters for Requests to thin out the Controller
    @Override
    public AirportEntity getAirportFromRequest(Map<String, Object> request, String key) {
        String airportCode = (String) request.get(key);
        return airportRepository.findAirportEntityByAirportCode(airportCode)
                .orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    @Override
    public PlaneEntity getPlaneFromRequest(Map<String, Object> request, String key) {
        String planeId = (String) request.get(key);
        return planeRepository.findById(planeId)
                .orElseThrow(() -> new RuntimeException("Plane not found"));
    }

    @Override
    public CompanyEntity getCompanyFromRequest(Map<String, Object> request, String key) {
        String companyId = (String) request.get(key);
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public LocalDateTime getDateTimeFromRequest(Map<String, Object> request, String key) {
        String dateTimeString = (String) request.get(key);
        return LocalDateTime.parse(dateTimeString);
    }

    // End of Getters for Requests


    // Getters for DTOs (Projections)
    @Override
    public VehicleTypeRepository.SeatingPlanProjection findSeatingPlanByFlightNumber(String flightNumber) {
        String vehicleType = flightRepository.findVehicleTypeByFlightId(flightNumber).getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.SeatingPlanProjection.class);
    }

    @Override
    public VehicleTypeRepository.AttendeeCapacityProjection findAttendeeCapacityByFlightNumber(String flightNumber) {
        String vehicleType = flightRepository.findVehicleTypeByFlightId(flightNumber).getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.AttendeeCapacityProjection.class);
    }

    @Override
    public VehicleTypeRepository.PilotCapacityProjection findPilotCapacityByFlightNumber(String flightNumber) {
        String vehicleType = flightRepository.findVehicleTypeByFlightId(flightNumber).getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.PilotCapacityProjection.class);
    }

}
