package com.su.FlightScheduler.APIs.FlightAPI;

import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.AdminRepository;
import com.su.FlightScheduler.Repository.AirportRepository;
import com.su.FlightScheduler.Repository.PlaneRepository;
import com.su.FlightScheduler.Repository.CompanyRepository;
import com.su.FlightScheduler.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;
    private final AdminRepository adminRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private CompanyRepository companyRepository;


    private AdminEntity getAdminFromRequest(Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        return adminRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    private AdminEntity getAdminFromRequestWithKey(Map<String, Object> request, String key) {
        Map<String, Object> adminDetails = (Map<String, Object>) request.get(key);
        String email = (String) adminDetails.get("email");
        String password = (String) adminDetails.get("password");
        return adminRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    private AirportEntity getAirportFromRequest(Map<String, Object> request, String key) {
    String airportCode = (String) request.get(key);
    return airportRepository.findAirportEntityByAirportCode(airportCode)
            .orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    private PlaneEntity getPlaneFromRequest(Map<String, Object> request, String key) {
        String planeId = (String) request.get(key);
        return planeRepository.findById(planeId)
                .orElseThrow(() -> new RuntimeException("Plane not found"));
    }

    private CompanyEntity getCompanyFromRequest(Map<String, Object> request, String key) {
        String companyId = (String) request.get(key);
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    private LocalDateTime getDateTimeFromRequest(Map<String, Object> request, String key) {
        String dateTimeString = (String) request.get(key);
        return LocalDateTime.parse(dateTimeString);
    }
    @Autowired
    public FlightController(FlightService flightService, AdminRepository adminRepository) {
        this.flightService = flightService;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/saveFlight")
    public ResponseEntity<FlightEntity> saveFlight(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        FlightEntity flight = (FlightEntity) request.get("flight");
        return ResponseEntity.ok(flightService.saveFlight(flight, admin));
    }

    @PostMapping("/createFlight")
    public ResponseEntity<FlightEntity> createFlight(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        String flightInfo = (String) request.get("flightInfo");
        return ResponseEntity.ok(flightService.createFlight(flightNumber, flightInfo, admin));
    }

    @GetMapping("/findFlightByNumber")
    public ResponseEntity<FlightEntity> findFlightByNumber(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        return ResponseEntity.ok(flightService.findFlightByNumber(flightNumber, admin).orElseThrow(() -> new RuntimeException("Flight not found")));
    }

    @GetMapping("/findAllFlights")
    public ResponseEntity<List<FlightEntity>> findAllFlights(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        return ResponseEntity.ok(flightService.findAllFlights(admin));
    }

    @PostMapping("/updateFlight")
    public ResponseEntity<FlightEntity> updateFlight(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        FlightEntity flight = (FlightEntity) request.get("flight");
        return ResponseEntity.ok(flightService.updateFlight(flight, admin));
    }

    @PostMapping("/deleteFlightByNumber")
    public ResponseEntity<Object> deleteFlightByNumber(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        flightService.deleteFlightByNumber(flightNumber, admin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateFlightInfo")
    public ResponseEntity<FlightEntity> updateFlightInfo(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        String flightInfo = (String) request.get("flightInfo");
        return ResponseEntity.ok(flightService.updateFlightInfo(flightNumber, flightInfo, admin));
    }

    @PostMapping("/updateSourceAirport")
    public ResponseEntity<FlightEntity> updateSourceAirport(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        AirportEntity sourceAirport = getAirportFromRequest(request, "sourceAirport");
        return ResponseEntity.ok(flightService.updateSourceAirport(flightNumber, sourceAirport, admin));
    }

    @PostMapping("/updateDestinationAirport")
    public ResponseEntity<FlightEntity> updateDestinationAirport(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        AirportEntity destinationAirport = getAirportFromRequest(request, "destinationAirport");
        return ResponseEntity.ok(flightService.updateDestinationAirport(flightNumber, destinationAirport, admin));
    }

    @PostMapping("/updatePlane")
    public ResponseEntity<FlightEntity> updatePlane(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        PlaneEntity plane = getPlaneFromRequest(request, "plane");
        return ResponseEntity.ok(flightService.updatePlane(flightNumber, plane, admin));
    }

    @PostMapping("/updateDepartureDateTime")
    public ResponseEntity<FlightEntity> updateDepartureDateTime(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        LocalDateTime departureDateTime = getDateTimeFromRequest(request, "departureDateTime");
        return ResponseEntity.ok(flightService.updateDepartureDateTime(flightNumber, departureDateTime, admin));
    }

    @PostMapping("/updateLandingDateTime")
    public ResponseEntity<FlightEntity> updateLandingDateTime(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        LocalDateTime landingDateTime = getDateTimeFromRequest(request, "landingDateTime");
        return ResponseEntity.ok(flightService.updateLandingDateTime(flightNumber, landingDateTime, admin));
    }

    @PostMapping("/updateSharedFlightCompany")
    public ResponseEntity<FlightEntity> updateSharedFlightCompany(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        CompanyEntity sharedFlightCompany = getCompanyFromRequest(request, "sharedFlightCompany");
        return ResponseEntity.ok(flightService.updateSharedFlightCompany(flightNumber, sharedFlightCompany, admin));
    }

    /*@PostMapping("/updateAdmin")
    public ResponseEntity<FlightEntity> updateAdmin(@RequestBody Map<String, Object> request) {
        AdminEntity admin = getAdminFromRequest(request);
        String flightNumber = (String) request.get("flightNumber");
        AdminEntity newAdmin = getAdminFromRequestWithKey(request, "admin");
        return ResponseEntity.ok(flightService.updateAdmin(flightNumber, newAdmin));
    }*/
}