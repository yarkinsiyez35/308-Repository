package com.su.FlightScheduler.DTO.SeatDTOs;

import java.util.List;

public class Seats {
    private List<SeatingTypeDTO> seatList;
    private List<SeatingDTO> seatingList;

    // getters and setters
    public List<SeatingTypeDTO> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<SeatingTypeDTO> seatList) {
        this.seatList = seatList;
    }

    public List<SeatingDTO> getSeatingList() {
        return seatingList;
    }

    public void setSeatingList(List<SeatingDTO> seatingList) {
        this.seatingList = seatingList;
    }
}