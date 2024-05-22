package com.su.FlightScheduler.DTO;

import com.su.FlightScheduler.Entity.PassengerFlight;

import java.util.Objects;

public class PassengerFlightDTO {

    private int bookingId;
    private String isParent;
    private String seatNumber;
    forPassenger_FlightDTO forPassenger_flightDTO;

    public PassengerFlightDTO() {}

    public PassengerFlightDTO(PassengerFlight passenger_flight) {
        this.bookingId = passenger_flight.getBookingId();
        this.isParent = passenger_flight.getIsParent();
        this.seatNumber = passenger_flight.getSeatNumber();
        this.forPassenger_flightDTO = new forPassenger_FlightDTO(passenger_flight.getFlight());
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public forPassenger_FlightDTO getForPassenger_flightDTO() {
        return forPassenger_flightDTO;
    }

    public void setForPassenger_flightDTO(forPassenger_FlightDTO forPassenger_flightDTO) {
        this.forPassenger_flightDTO = forPassenger_flightDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerFlightDTO that = (PassengerFlightDTO) o;
        return getBookingId() == that.getBookingId() && Objects.equals(getIsParent(), that.getIsParent()) && Objects.equals(getSeatNumber(), that.getSeatNumber()) && Objects.equals(getForPassenger_flightDTO(), that.getForPassenger_flightDTO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookingId(), getIsParent(), getSeatNumber(), getForPassenger_flightDTO());
    }
}
