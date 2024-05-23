package com.su.FlightScheduler.DTO.SeatDTOs;

public class SeatingDTO {
    private String seatPosition;
    private String seatType;
    private boolean status;
    private int userId;

    // Getters and Setters
    public String getSeatPosition() {
    return seatPosition;
    }

    public void setSeatPosition(String seatPosition) {
        this.seatPosition = seatPosition;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}