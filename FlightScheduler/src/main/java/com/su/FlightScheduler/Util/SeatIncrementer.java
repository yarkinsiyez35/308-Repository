package com.su.FlightScheduler.Util;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;

import java.util.List;

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

    public static String findLastPilotSeat(List<PilotAssignmentEntity> pilotAssignmentEntityList)
    {
        String lastSeat = pilotAssignmentEntityList.get(0).getSeatNumber();
        for (PilotAssignmentEntity pilotAssignmentEntity: pilotAssignmentEntityList)
        {
            String currentSeat = pilotAssignmentEntity.getSeatNumber();
            if (compareSeats(currentSeat,lastSeat) > 0)
            {
                lastSeat = currentSeat;
            }
        }
        return lastSeat;
    }

    public static String findLastCabinCrewSeat(List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList) {
        String lastSeat = cabinCrewAssignmentsEntityList.get(0).getSeatNumber();

        for (CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity : cabinCrewAssignmentsEntityList) {
            String currentSeat = cabinCrewAssignmentsEntity.getSeatNumber();
            if (compareSeats(currentSeat, lastSeat) > 0) {
                lastSeat = currentSeat;
            }
        }
        return lastSeat;
    }

    private static int compareSeats(String seat1, String seat2) {
        // Split the seat into numeric and alphabetic parts
        int row1 = Integer.parseInt(seat1.replaceAll("\\D+", ""));
        int row2 = Integer.parseInt(seat2.replaceAll("\\D+", ""));
        char col1 = seat1.replaceAll("\\d+", "").charAt(0);
        char col2 = seat2.replaceAll("\\d+", "").charAt(0);

        if (row1 != row2) {
            return row1 - row2;
        }

        return col1 - col2;
    }
}
