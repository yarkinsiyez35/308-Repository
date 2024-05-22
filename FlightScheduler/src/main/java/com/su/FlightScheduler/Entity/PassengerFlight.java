package com.su.FlightScheduler.Entity;

import jakarta.persistence.*;

import java.util.Objects;


/*
CREATE TABLE passenger_flights(
    passenger_id INT NOT NULL,
    booking_id VARCHAR(255) UNIQUE NOT NULL,
    flight_number VARCHAR(6) NOT NULL,
    is_parent VARCHAR(1) NOT NULL,
    seat_number VARCHAR(255) NOT NULL,
    FOREIGN KEY (passenger_id) REFERENCES passengers(passenger_id),
    FOREIGN KEY (flight_number) REFERENCES flights(flight_number),
    PRIMARY KEY (passenger_id, flight_number)
);
 */

@Entity
@Table(name = "passenger_flights")
public class PassengerFlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passenger;

    @ManyToOne
    @JoinColumn(name = "flight_number", nullable = false)
    private FlightEntity flight;

    @Column(name = "is_parent")
    private String isParent;

    @Column(name = "seat_number")
    private String seatNumber;

    // Getters and Setters
    public PassengerFlight() {

    }
    /*
    public PassengerFlight(PassengerEntity passenger, FlightEntity flight,
                           String isParent, String seatNumber,
                           int bookingId) {
        this.passenger = passenger;
        this.flight = flight;
        this.isParent = isParent;
        this.seatNumber = seatNumber;
        this.bookingId = bookingId;
    }
     */

    public PassengerFlight(PassengerEntity passenger, FlightEntity flight,
                           String isParent, String seatNumber) {
        this.passenger = passenger;
        this.flight = flight;
        this.isParent = isParent;
        this.seatNumber = seatNumber;
    }

    public PassengerFlight(PassengerFlight passengerFlight){
        this.passenger = passengerFlight.getPassenger();
        this.flight = passengerFlight.getFlight();
        this.isParent = passengerFlight.getIsParent();
        this.seatNumber = passengerFlight.getSeatNumber();
        this.bookingId = passengerFlight.getBookingId();
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String parent) {
        isParent = parent;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerFlight that = (PassengerFlight) o;
        return getBookingId() == that.getBookingId() &&
                Objects.equals(getPassenger(), that.getPassenger()) &&
                Objects.equals(getFlight(), that.getFlight()) &&
                Objects.equals(getIsParent(), that.getIsParent()) &&
                Objects.equals(getSeatNumber(), that.getSeatNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookingId(), getPassenger(), getFlight(), getIsParent(), getSeatNumber());
    }
}

