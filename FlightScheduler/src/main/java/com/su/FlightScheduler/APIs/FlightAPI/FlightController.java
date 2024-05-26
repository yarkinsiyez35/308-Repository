package com.su.FlightScheduler.APIs.FlightAPI;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.Seats;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Entity.FlightEntitites.AirportEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.PlaneEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TESTING: this controller should be tested
@RestController
@RequestMapping("/api/flights")
@CrossOrigin(value = "http://127.0.0.1:5500", allowCredentials = "true")
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



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    @PostMapping("/createFlight/{adminId}")
    public ResponseEntity<Object> saveFlightFromDTO(@RequestBody FlightDataDTO flightDataDTO, @PathVariable int adminId) {
        try {
            FlightEntity savedFlight = flightService.createFlight(flightDataDTO, adminId);
            FlightDataDTO savedFlightDTO = new FlightDataDTO(savedFlight);
            return ResponseEntity.ok(savedFlightDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }


    @PostMapping("/saveFlight")
    public ResponseEntity<?> saveFlight(@RequestBody FlightEntity flight) {
        try {
            return ResponseEntity.ok(flightService.saveFlightObj(flight));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    // --- Find Methods ---

    @GetMapping("/findAllFlights")
    public ResponseEntity<?> findAllFlights() {
        try {
            return ResponseEntity.ok(flightService.findAllFlights());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No flights found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findFlightByNumber") // This should return a FlightDataDTO
    public ResponseEntity<?> findFlightByNumber(@RequestParam String flightNumber) {
        try {
            return ResponseEntity.ok(flightService.findFlightByNumberDTO(flightNumber));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findAllFlightsDTO")
    public ResponseEntity<Object> getAllFlights() {
        List<FlightDataDTO> flightDataDTOList = flightService.findAllFlightsDTO();
        return ResponseEntity.ok(flightDataDTOList);
    }

    @GetMapping("/departureAirport")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureAirport(@RequestParam String airportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAirport(airportCode));
    }

    @GetMapping("/destinationAirport")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDestinationAirport(@RequestParam String airportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDestinationAirport(airportCode));
    }

    @GetMapping("/departureAndDestinationAirport")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureAndDestinationAirport(@RequestParam String departureAirportCode, @RequestParam String destinationAirportCode) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndDestinationAirport(departureAirportCode, destinationAirportCode));
    }

    @GetMapping("/departureDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureDateTime(@RequestParam LocalDateTime departureDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureDateTime(departureDateTime));
    }

    @GetMapping("/landingDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByLandingDateTime(@RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByLandingDateTime(landingDateTime));
    }

    @GetMapping("/departureAndLandingDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureAndLandingDateTime(@RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndLandingDateTime(departureDateTime, landingDateTime));
    }

    @GetMapping("/departureAirportAndDepartureDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureAirportAndDepartureDateTime(@RequestParam String airportCode, @RequestParam LocalDateTime departureDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAirportAndDepartureDateTime(airportCode, departureDateTime));
    }

    @GetMapping("/destinationAirportAndLandingDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDestinationAirportAndLandingDateTime(@RequestParam String airportCode, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDestinationAirportAndLandingDateTime(airportCode, landingDateTime));
    }

    @GetMapping("/departureAndDestinationAirportAndDepartureAndLandingDateTime")
    public ResponseEntity<List<FlightDataDTO>> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(@RequestParam String departureAirportCode, @RequestParam String destinationAirportCode, @RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime landingDateTime) {
        return ResponseEntity.ok(flightService.findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime));
    }

    // --- End of Find Methods ---

    // --- Update Methods ---
    @PostMapping("/updateFlight/{adminId}")
    public ResponseEntity<?> updateFlightByFightDTO(@RequestBody FlightDataDTO flight, @PathVariable int adminId) {
        return ResponseEntity.ok(flightService.updateFlightByFlightDTO(flight, adminId));
    }

    @DeleteMapping("/deleteFlightByNumber")
    public ResponseEntity<?> deleteFlightByNumber(@RequestParam String flightNumber) {
        flightService.deleteFlightByNumber(flightNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateFlightInfo")
    public ResponseEntity<?> updateFlightInfo(@RequestParam String flightNumber, @RequestParam String flightInfo) {
        if (flightInfo == null || flightInfo.isEmpty()) {
            throw new BadRequestException("Flight info cannot be null or empty");
        }
        return ResponseEntity.ok(flightService.updateFlightInfo(flightNumber, flightInfo));
    }

    @PostMapping("/updateSourceAirport")
    public ResponseEntity<?> updateSourceAirport(@RequestParam String flightNumber, @RequestBody AirportEntity sourceAirport) {
        if (sourceAirport == null) {
            throw new RuntimeException("Source airport cannot be null");
        }
        return ResponseEntity.ok(flightService.updateSourceAirport(flightNumber, sourceAirport));
    }

    @PostMapping("/updateDestinationAirport")
    public ResponseEntity<?> updateDestinationAirport(@RequestParam String flightNumber, @RequestBody AirportEntity destinationAirport) {
        if (destinationAirport == null) {
            throw new RuntimeException("Destination airport cannot be null");
        }
        return ResponseEntity.ok(flightService.updateDestinationAirport(flightNumber, destinationAirport));
    }

    @PostMapping("/updatePlane")
    public ResponseEntity<?> updatePlane(@RequestParam String flightNumber, @RequestBody PlaneEntity plane) {
        if (plane == null) {
            throw new RuntimeException("Plane cannot be null");
        }
        return ResponseEntity.ok(flightService.updatePlane(flightNumber, plane));
    }

    @PostMapping("/updateDepartureDateTime")
    public ResponseEntity<?> updateDepartureDateTime(@RequestParam String flightNumber, @RequestParam String departureDateTime) {
        LocalDateTime departureDateTimeParsed = LocalDateTime.parse(departureDateTime);
        if (departureDateTimeParsed.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Departure time cannot be in the past");
        }
        return ResponseEntity.ok(flightService.updateDepartureDateTime(flightNumber, departureDateTimeParsed));
    }

    @PostMapping("/updateLandingDateTime")
    public ResponseEntity<?> updateLandingDateTime(@RequestParam String flightNumber, @RequestParam String landingDateTime) {
        LocalDateTime landingDateTimeParsed = LocalDateTime.parse(landingDateTime);
        FlightEntity flight = flightService.getFlightOrThrow(flightNumber);
        if (landingDateTimeParsed.isBefore(flight.getDepartureDateTime())) {
            throw new RuntimeException("Landing time cannot be before departure time");
        }
        return ResponseEntity.ok(flightService.updateLandingDateTime(flightNumber, landingDateTimeParsed));
    }

    @PostMapping("/updateSharedFlightCompany")
    public ResponseEntity<?> updateSharedFlightCompany(@RequestParam String flightNumber, @RequestBody CompanyEntity sharedFlightCompany) {
        if (sharedFlightCompany == null) {
            throw new BadRequestException("Shared flight company cannot be null");
        }
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

    @GetMapping("/{flightId}/seats")
    public Seats getSeats(@PathVariable String flightId) {
        List<SeatingTypeDTO> seatList = flightService.decodeSeatingPlan(flightId);
        List<SeatingDTO> seatingList = flightService.findBookedFlightsByFlightNumber(flightId);

        Seats seats = new Seats();
        seats.setSeatList(seatList);
        seats.setSeatingList(seatingList);

        return seats;
    }
}
