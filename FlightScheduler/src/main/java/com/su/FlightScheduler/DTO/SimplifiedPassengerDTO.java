package com.su.FlightScheduler.DTO;


import com.su.FlightScheduler.Entity.PassengerEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimplifiedPassengerDTO {

    private int passengerId;
    private String firstName;
    private String surname;
    private List<PassengerFlightDTO> passengerFlights;

    public SimplifiedPassengerDTO() {}
    public SimplifiedPassengerDTO(PassengerEntity passengerEntity) {
        this.passengerId = passengerEntity.getPassengerId();
        this.firstName = passengerEntity.getFirstName();
        this.surname = passengerEntity.getSurname();

        this.passengerFlights = passengerEntity.getPassengerFlights()
                .stream()
                .map(PassengerFlightDTO::new)
                .collect(Collectors.toList());
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public List<PassengerFlightDTO> getPassengerFlights() {
        return passengerFlights;
    }

    public void setPassengerFlights(List<PassengerFlightDTO> passengerFlights) {
        this.passengerFlights = passengerFlights;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplifiedPassengerDTO that = (SimplifiedPassengerDTO) o;
        return getPassengerId() == that.getPassengerId() && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getPassengerFlights(), that.getPassengerFlights());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassengerId(), getFirstName(), getSurname(), getPassengerFlights());
    }
}
