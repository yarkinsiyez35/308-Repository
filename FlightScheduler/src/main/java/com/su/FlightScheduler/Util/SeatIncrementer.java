package com.su.FlightScheduler.Util;

public class SeatIncrementer {
    public static String incrementSeat(String lastSeat) {
        // Extract the numeric part and the letter part
        String numericPart = lastSeat.substring(0, lastSeat.length() - 1);
        char seatLetter = lastSeat.charAt(lastSeat.length() - 1);

        // Increment the seat letter
        char newSeatLetter = (char) (seatLetter + 1);

        // Combine the numeric part and the new seat letter
        String newSeat = numericPart + newSeatLetter;

        return newSeat;
    }
}
