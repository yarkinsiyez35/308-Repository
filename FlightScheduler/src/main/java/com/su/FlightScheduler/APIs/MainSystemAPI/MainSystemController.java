package com.su.FlightScheduler.APIs.MainSystemAPI;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Service.AttendantAssignmentService;
import com.su.FlightScheduler.Service.PassengerFlightService;
import com.su.FlightScheduler.Service.PilotFlightAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import com.su.FlightScheduler.Service.FlightService;

//TESTING: this controller should be tested
@RestController
@RequestMapping("/main")
@CrossOrigin(value = "http://127.0.0.1:5500", allowCredentials = "true")
public class MainSystemController {
    private final PilotFlightAssignmentService pilotFlightAssignmentService;
    private final AttendantAssignmentService attendantAssignmentService;
    private final PassengerFlightService passengerFlightService;

    private final FlightService flightService;

    @Autowired
    public MainSystemController(PilotFlightAssignmentService pilotFlightAssignmentService, AttendantAssignmentService attendantAssignmentService, PassengerFlightService passengerFlightService, FlightService flightService) {
        this.pilotFlightAssignmentService = pilotFlightAssignmentService;
        this.attendantAssignmentService = attendantAssignmentService;
        this.passengerFlightService = passengerFlightService;
        this.flightService = flightService;
    }

    //functions for pilot and flight assignments
    @GetMapping("/pilot/{pilotId}/getFlights")
    public ResponseEntity<Object> getFlightsOfPilot(@PathVariable int pilotId)
    {  //this function returns the assignments of a given pilot
        try
        {
            UserDataDTO results = pilotFlightAssignmentService.getFlightsOfPilot(pilotId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/flight/{flightId}/getAvailablePilots")
    public ResponseEntity<Object> getAvailablePilotsForFlight(@PathVariable String flightId)
    {   //this function returns the available pilots for a given flight
        try
        {
            List<UserDataDTO> results = pilotFlightAssignmentService.getAvailablePilotsForFlight(flightId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getPilots")
    public ResponseEntity<Object> getPilotsOfFlight(@PathVariable String flightId)
    {   //this function returns the active pilots for a given flight
        try
        {
            List<UserDataDTO> userDataDTOList = pilotFlightAssignmentService.getPilotsOfFlight(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/pilot/{pilotId}/assignToFlight/{flightId}")
    public ResponseEntity<Object> assignPilotToFlight(@PathVariable int pilotId, @PathVariable String flightId)
    {   //this function assigns the pilot to a given flight and returns the updated pilot information
        try
        {
            UserDataDTO userDataDTO = pilotFlightAssignmentService.assignPilotToFlight(flightId, pilotId);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/pilot/{pilotId}/removeFromFlight/{flightId}")
    public ResponseEntity<Object> removePilotFromFlight(@PathVariable int pilotId, @PathVariable String flightId)
    {   //this function removes the pilot from the given flight and automatically assigns a replacement for the flight
        try
        {
            UserDataDTO userDataDTO = pilotFlightAssignmentService.removeFlightFromAPilot(flightId,pilotId);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //functions for cabin crew and flight assignments
    @GetMapping("/attendant/{attendantId}/getFlights")
    public ResponseEntity<Object> getFlightAssignmentsOfAttendant(@PathVariable int attendantId)
    {   //this function returns the assignments of a given attendant
        try
        {
            UserDataDTO results = attendantAssignmentService.getFlightsOfAttendant(attendantId);
            return ResponseEntity.ok(results);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getAvailableAttendants")
    public ResponseEntity<Object> getAvailableAttendantsForFlight(@PathVariable String flightId)
    {   //this function returns the available attendants for a given flight
        try
        {
            List<UserDataDTO> results = attendantAssignmentService.getAvailableAttendantsForFlight(flightId);
            return ResponseEntity.ok(results);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/flight/{flightId}/getAttendants")
    public ResponseEntity<Object> getAttendantsOfFlight(@PathVariable String flightId)
    {   //this function returns the active attendants for a given flight
        try{

            List<UserDataDTO> userDataDTOList = attendantAssignmentService.getAttendantsOfFlight(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/attendant/{attendantId}/assignToFlight/{flightId}")
    public ResponseEntity<Object> assignAttendantToFlight(@PathVariable int attendantId, @PathVariable String flightId)
    {   //this function assigns the attendant for a given flight and returns the updated attendant information
        try{
            UserDataDTO userDataDTO = attendantAssignmentService.assignAttendantToFlight(flightId, attendantId);
            return  ResponseEntity.ok(userDataDTO);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/attendant/{attendantId}/removeFromFlight/{flightNumber}")
    public ResponseEntity<Object> removeAttendantFromFlight(@PathVariable int attendantId, @PathVariable String flightNumber)
    {
        try
        {
            UserDataDTO userDataDTO = attendantAssignmentService.removeAttendantFromFlight(flightNumber,attendantId);
            return ResponseEntity.ok(userDataDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //functions for passenger and flight assignments

    @GetMapping("/passenger/{passengerId}/getFlights")
    public ResponseEntity<Object> getFlightsOfPassenger(@PathVariable int passengerId)
    {
        try {
            UserDataDTO flights = passengerFlightService.findBookedFlightsByPassengerId(passengerId);
            return ResponseEntity.ok(flights);
        }
        catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /*@GetMapping("/flight/{flightId}/getPassengers")
    public ResponseEntity<Object> getPassengersOfFlight(@PathVariable String flightId)
    {
        return null;
    }*/

    @PostMapping("/passenger/{passengerId}/bookFlight/{flightNumber}/{isParent}")
    public ResponseEntity<Object> assignPassengerToFlight(@PathVariable int passengerId, @PathVariable String flightNumber, @PathVariable String isParent, @RequestBody SeatingDTO seatingDTO)
    {
        try {
            PassengerFlight passengerFlight = passengerFlightService.bookFlight( //passengerId, flightNumber, isParent, seatNumber );
                    passengerId,
                    flightNumber,
                    isParent,
                    seatingDTO.getSeatPosition()
            );

            //PassengerFlightDTO passengerFlightDTO = new PassengerFlightDTO(passengerFlight);
            UserDataDTO userDataDTO = new UserDataDTO(passengerFlight);

            return ResponseEntity.ok(userDataDTO);

        } catch (RuntimeException e) {//expected

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());

        } catch (Exception e) { //should be unreachable

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @PostMapping("/passenger/{passengerId}/bookFlightAuto/{flightNumber}/{isParent}/{isEconomy}")
    public ResponseEntity<Object> assignPassengerToFlightAutomatically(@PathVariable Boolean isEconomy, @PathVariable int passengerId, @PathVariable String flightNumber, @PathVariable String isParent)
    {
        try {
            PassengerFlight passengerFlight = passengerFlightService.bookFlightAuto(
                    passengerId,
                    flightNumber,
                    isParent,
                    isEconomy
            );

            UserDataDTO userDataDTO = new UserDataDTO(passengerFlight);

            return ResponseEntity.ok(userDataDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/passenger/{passengerId}/cancelBooking/{bookingId}")
    public ResponseEntity<Object> deleteFlightFromPassenger(@PathVariable int passengerId, @PathVariable int bookingId)
    {
        try
        {
            PassengerFlight passengerFlight = passengerFlightService.cancelFlight(bookingId);
            return ResponseEntity.ok(new UserDataDTO(passengerFlight));
        } catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("flight/{flightId}/getPassengers")
    public ResponseEntity<Object> getPassengersOfFlight(@PathVariable String flightId)
    {
        try
        {
            List<UserDataDTO> userDataDTOList = flightService.getUsersDTOByFlightNumber(flightId);
            return ResponseEntity.ok(userDataDTOList);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
