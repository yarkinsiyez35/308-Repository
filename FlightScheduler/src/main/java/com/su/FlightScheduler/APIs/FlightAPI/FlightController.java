package com.su.FlightScheduler.APIs.FlightAPI;

import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.AirportRepository;
import com.su.FlightScheduler.Repository.PlaneRepository;
import com.su.FlightScheduler.Repository.CompanyRepository;
import com.su.FlightScheduler.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private CompanyRepository companyRepository;



    // Constructor
    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // API Endpoints
    @PostMapping("/saveFlight")
    public ResponseEntity<?> saveFlight(@RequestBody FlightEntity flight) {
        try {
            return ResponseEntity.ok(flightService.saveFlightObj(flight));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createFlightFilled")
    public ResponseEntity<?> createFlightFilled(@RequestBody Map<String, Object> request) {
        try {
            String flightNumber = (String) request.get("flightNumber");
            String flightInfo = (String) request.get("flightInfo");
            AdminEntity admin = (AdminEntity) request.get("admin");
            return ResponseEntity.ok(flightService.createFlight(flightNumber, flightInfo, admin));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createFlight")
    public ResponseEntity<?> createFlight(@RequestBody Map<String, Object> request) {
        try {
            String flightNumber = (String) request.get("flightNumber");
            String flightInfo = (String) request.get("flightInfo");
            AdminEntity admin = (AdminEntity) request.get("admin");
            FlightEntity flight = flightService.createFlight(flightNumber, flightInfo, admin);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addFlightParams1")
    public ResponseEntity<?> addFlightParams1(@RequestBody Map<String, Object> request) {
        try {
            String flightNumber = (String) request.get("flightNumber");
            PlaneEntity plane = flightService.getPlaneFromRequest(request, "plane");
            AirportEntity sourceAirport = flightService.getAirportFromRequest(request, "sourceAirport");
            AirportEntity destinationAirport = flightService.getAirportFromRequest(request, "destinationAirport");
            FlightEntity flight = flightService.addFlightParams1(flightNumber, plane, sourceAirport, destinationAirport);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addFlightParams2")
    public ResponseEntity<?> addFlightParams2(@RequestBody Map<String, Object> request) {
        try {
            String flightNumber = (String) request.get("flightNumber");
            Integer flightRange = (Integer) request.get("flightRange");
            LocalDateTime departureDateTime = flightService.getDateTimeFromRequest(request, "departureDateTime");
            LocalDateTime landingDateTime = flightService.getDateTimeFromRequest(request, "landingDateTime");
            FlightEntity flight = flightService.addFlightParams2(flightNumber, flightRange, departureDateTime, landingDateTime);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addFlightParams3")
    public ResponseEntity<?> addFlightParams3(@RequestBody Map<String, Object> request) {
        try {
            String flightNumber = (String) request.get("flightNumber");
            boolean sharedFlight = (boolean) request.get("sharedFlight");
            CompanyEntity sharedFlightCompany = flightService.getCompanyFromRequest(request, "sharedFlightCompany");
            FlightEntity flight = flightService.addFlightParams3(flightNumber, sharedFlight, sharedFlightCompany);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findFlightByNumber")
    public ResponseEntity<?> findFlightByNumber(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.findFlightByNumber(flightNumber).orElseThrow(() -> new RuntimeException("Flight not found")));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findAllFlights")
    public ResponseEntity<?> findAllFlights() {
        try {
            return ResponseEntity.ok(flightService.findAllFlights());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No flights found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateFlight")
    public ResponseEntity<?> updateFlight(@RequestBody FlightEntity flight) {
        try {
            return ResponseEntity.ok(flightService.updateFlight(flight));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deleteFlightByNumber")
    public ResponseEntity<?> deleteFlightByNumber(@RequestParam String flightNumber) {
        try {
            flightService.deleteFlightByNumber(flightNumber);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateFlightInfo")
    public ResponseEntity<?> updateFlightInfo(@RequestParam String flightNumber, @RequestParam String flightInfo) {
        try {
            return ResponseEntity.ok(flightService.updateFlightInfo(flightNumber, flightInfo));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateSourceAirport")
    public ResponseEntity<?> updateSourceAirport(@RequestParam String flightNumber, @RequestBody AirportEntity sourceAirport) {
        try {
            return ResponseEntity.ok(flightService.updateSourceAirport(flightNumber, sourceAirport));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateDestinationAirport")
    public ResponseEntity<?> updateDestinationAirport(@RequestParam String flightNumber, @RequestBody AirportEntity destinationAirport) {
        try {
            return ResponseEntity.ok(flightService.updateDestinationAirport(flightNumber, destinationAirport));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updatePlane")
    public ResponseEntity<?> updatePlane(@RequestParam String flightNumber, @RequestBody PlaneEntity plane) {
        try {
            return ResponseEntity.ok(flightService.updatePlane(flightNumber, plane));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateDepartureDateTime")
    public ResponseEntity<?> updateDepartureDateTime(@RequestParam String flightNumber, @RequestParam String departureDateTime) {
        try {
            LocalDateTime departureDateTimeParsed = LocalDateTime.parse(departureDateTime);
            return ResponseEntity.ok(flightService.updateDepartureDateTime(flightNumber, departureDateTimeParsed));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateLandingDateTime")
    public ResponseEntity<?> updateLandingDateTime(@RequestParam String flightNumber, @RequestParam String landingDateTime) {
        try {
            LocalDateTime landingDateTimeParsed = LocalDateTime.parse(landingDateTime);
            return ResponseEntity.ok(flightService.updateLandingDateTime(flightNumber, landingDateTimeParsed));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateSharedFlightCompany")
    public ResponseEntity<?> updateSharedFlightCompany(@RequestParam String flightNumber, @RequestBody CompanyEntity sharedFlightCompany) {
        try {
            return ResponseEntity.ok(flightService.updateSharedFlightCompany(flightNumber, sharedFlightCompany));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getSourceAirport")
    public ResponseEntity<?> getSourceAirport(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.getSourceAirport(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getDestinationAirport")
    public ResponseEntity<?> getDestinationAirport(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.getDestinationAirport(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getPlane")
    public ResponseEntity<?> getPlane(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.getPlane(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getCompany")
    public ResponseEntity<?> getCompany(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.getCompany(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getDateTime")
    public ResponseEntity<?> getDateTime(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.getDateTime(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }


}
