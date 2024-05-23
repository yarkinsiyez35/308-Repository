package com.su.FlightScheduler.APIs.FlightAPI;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.Seats;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.AirportRepository;
import com.su.FlightScheduler.Repository.PlaneRepository;
import com.su.FlightScheduler.Repository.CompanyRepository;
import com.su.FlightScheduler.Service.FlightService;
import jakarta.persistence.EntityNotFoundException;
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


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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

    // --- Create Methods ---
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
    // --- End of Create Methods ---


    // --- Find Methods ---
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

    @GetMapping("/departureAirport")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAirport(@RequestParam String airportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAirport(airportCode));
    }

    @GetMapping("/destinationAirport")
    public ResponseEntity<List<FlightEntity>> findFlightsByDestinationAirport(@RequestParam String airportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDestinationAirport(airportCode));
    }

    @GetMapping("/departureAndDestinationAirport")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAndDestinationAirport(@RequestParam String departureAirportCode, @RequestParam String destinationAirportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndDestinationAirport(departureAirportCode, destinationAirportCode));
    }

    @GetMapping("/departureDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureDateTime(@RequestParam LocalDateTime departureDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureDateTime(departureDateTime));
    }

    @GetMapping("/landingDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByLandingDateTime(@RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByLandingDateTime(landingDateTime));
    }

    @GetMapping("/departureAndLandingDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAndLandingDateTime(@RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndLandingDateTime(departureDateTime, landingDateTime));
    }

    @GetMapping("/departureAirportAndDepartureDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAirportAndDepartureDateTime(@RequestParam String airportCode, @RequestParam LocalDateTime departureDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAirportAndDepartureDateTime(airportCode, departureDateTime));
    }

    @GetMapping("/destinationAirportAndLandingDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDestinationAirportAndLandingDateTime(@RequestParam String airportCode, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDestinationAirportAndLandingDateTime(airportCode, landingDateTime));
    }

    @GetMapping("/departureAndDestinationAirportAndDepartureAndLandingDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(@RequestParam String departureAirportCode, @RequestParam String destinationAirportCode, @RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime));
    }

    @GetMapping("/departureAirportAndDestinationAirportAndDepartureAndLandingDateTime")
    public ResponseEntity<List<FlightEntity>> findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime(@RequestParam String departureAirportCode, @RequestParam String destinationAirportCode, @RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime));
    }

    // --- End of Find Methods ---

    // --- Update Methods ---
    @PostMapping("/updateFlight")
    public ResponseEntity<?> updateFlight(@RequestBody FlightEntity flight) {
        return ResponseEntity.ok(flightService.updateFlight(flight));
    }

    @PostMapping("/deleteFlightByNumber")
    public ResponseEntity<?> deleteFlightByNumber(@RequestParam String flightNumber) {
        flightService.deleteFlightByNumber(flightNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateFlightInfo")
    public ResponseEntity<?> updateFlightInfo(@RequestParam String flightNumber, @RequestParam String flightInfo) {
        return ResponseEntity.ok(flightService.updateFlightInfo(flightNumber, flightInfo));
    }

    @PostMapping("/updateSourceAirport")
    public ResponseEntity<?> updateSourceAirport(@RequestParam String flightNumber, @RequestBody AirportEntity sourceAirport) {
        return ResponseEntity.ok(flightService.updateSourceAirport(flightNumber, sourceAirport));
    }

    @PostMapping("/updateDestinationAirport")
    public ResponseEntity<?> updateDestinationAirport(@RequestParam String flightNumber, @RequestBody AirportEntity destinationAirport) {
        return ResponseEntity.ok(flightService.updateDestinationAirport(flightNumber, destinationAirport));
    }

    @PostMapping("/updatePlane")
    public ResponseEntity<?> updatePlane(@RequestParam String flightNumber, @RequestBody PlaneEntity plane) {
        return ResponseEntity.ok(flightService.updatePlane(flightNumber, plane));
    }

    @PostMapping("/updateDepartureDateTime")
    public ResponseEntity<?> updateDepartureDateTime(@RequestParam String flightNumber, @RequestParam String departureDateTime) {
        LocalDateTime departureDateTimeParsed = LocalDateTime.parse(departureDateTime);
        return ResponseEntity.ok(flightService.updateDepartureDateTime(flightNumber, departureDateTimeParsed));
    }

    @PostMapping("/updateLandingDateTime")
    public ResponseEntity<?> updateLandingDateTime(@RequestParam String flightNumber, @RequestParam String landingDateTime) {
        LocalDateTime landingDateTimeParsed = LocalDateTime.parse(landingDateTime);
        return ResponseEntity.ok(flightService.updateLandingDateTime(flightNumber, landingDateTimeParsed));
    }

    @PostMapping("/updateSharedFlightCompany")
    public ResponseEntity<?> updateSharedFlightCompany(@RequestParam String flightNumber, @RequestBody CompanyEntity sharedFlightCompany) {
        return ResponseEntity.ok(flightService.updateSharedFlightCompany(flightNumber, sharedFlightCompany));
    }
    // --- End of Update Methods ---

    // --- Getters for the entities ---
    @GetMapping("/getSourceAirport")
    public ResponseEntity<?> getSourceAirport(@RequestParam String flightNumber) {
        return ResponseEntity.ok(flightService.getSourceAirport(flightNumber));
    }

    @GetMapping("/getDestinationAirport")
    public ResponseEntity<?> getDestinationAirport(@RequestParam String flightNumber) {
        return ResponseEntity.ok(flightService.getDestinationAirport(flightNumber));
    }

    @GetMapping("/getPlane")
    public ResponseEntity<?> getPlane(@RequestParam String flightNumber) {
        return ResponseEntity.ok(flightService.getPlane(flightNumber));
    }

    @GetMapping("/getCompany")
    public ResponseEntity<?> getCompany(@RequestParam String flightNumber) {
        return ResponseEntity.ok(flightService.getCompany(flightNumber));
    }

    @GetMapping("/getDateTime")
    public ResponseEntity<?> getDateTime(@RequestParam String flightNumber) {
        return ResponseEntity.ok(flightService.getDateTime(flightNumber));
    }

    // --- End of Getters for the entities ---

    @GetMapping("/flights/{flightId}/seats")
    public Seats getSeats(@PathVariable String flightId) {
        List<SeatingTypeDTO> seatList = flightService.decodeSeatingPlan(flightId);
        List<SeatingDTO> seatingList = flightService.findBookedFlightsByFlightNumber(flightId);

        Seats seats = new Seats();
        seats.setSeatList(seatList);
        seats.setSeatingList(seatingList);

        return seats;
    }
}
