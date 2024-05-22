package com.su.FlightScheduler.APIs;


import com.su.FlightScheduler.DTO.PassengerFlightDTO;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Service.PassengerFlightServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final PassengerFlightServiceImp bookingService;

    @Autowired
    public BookingController(PassengerFlightServiceImp bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Object> bookFlight(@RequestBody PassengerFlight booking)
                                             /*
                                            @RequestParam int passengerId,
                                            @RequestParam String flightNumber,
                                            @RequestParam String isParent,
                                            @RequestParam String seatNumber)*/
    {

        try {

            PassengerFlight passengerFlight = bookingService.bookFlight( //passengerId, flightNumber, isParent, seatNumber );

                    booking.getPassenger().getPassengerId(),
                    booking.getFlight().getFlightNumber(),
                    booking.getIsParent(),
                    booking.getSeatNumber()

            );


            PassengerFlightDTO passengerFlightDTO = new PassengerFlightDTO(passengerFlight);

            return ResponseEntity.ok(passengerFlightDTO);

        } catch (RuntimeException e) {//expected

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());

        } catch (Exception e) { //should be unreachable

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    /*
    @GetMapping("/{flightNumber}/passengers")
    public ResponseEntity<Object> findPassengersByFlightNumber(@PathVariable String flightNumber) {
        try {
            List<PassengerFlight> passengers = bookingService.findBookedFlightsByFlightNumber(flightNumber);
            return ResponseEntity.ok(passengers);
        }
        catch (RuntimeException e) {
            if (e instanceof NoSuchElementException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
     */


    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<Object> cancelFlight(@PathVariable int bookingId) {
        try {
            PassengerFlightDTO passengerFlight = bookingService.cancelFlight(bookingId);
            return ResponseEntity.ok(passengerFlight);
        } catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/{bookingId}")
    public ResponseEntity<Object> findBookingById(@PathVariable int bookingId)
    {
        try {
            PassengerFlightDTO passengerFlightDTO = bookingService.findBookingById(bookingId);
            return ResponseEntity.ok(passengerFlightDTO);
        }
        catch (RuntimeException e) { //expected
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) { //should be unreachable
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<PassengerFlightDTO>> findAllBookings()
    {
        List<PassengerFlightDTO> bookings = bookingService.findAllBookings();
        return ResponseEntity.ok(bookings);
    }
}