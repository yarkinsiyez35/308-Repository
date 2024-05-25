package com.su.FlightScheduler.Util;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class SeatTypeFinder {

    //this code is taken from FlightServiceImp
    public static List<SeatingTypeDTO> decodeSeatingType(String seatingPlan)
    {
        List<SeatingTypeDTO> seatingList = new ArrayList<>();

        // Split the encoded seating plan into business and economy seating types
        String[] seatingClasses = seatingPlan.split("=");

        for (String seatingClass : seatingClasses) {
            // Split the seating class into individual seating types
            String[] seatingTypes = seatingClass.split("\\*");

            for (String seatingType : seatingTypes) {
                SeatingTypeDTO seating = new SeatingTypeDTO();

                // Split the seating type into rows and columns
                String[] rowsAndColumns = seatingType.split("\\|");

                // Set the start row, end row, and columns for this seating type
                seating.setStartRow(Integer.parseInt(rowsAndColumns[0]));
                seating.setEndRow(Integer.parseInt(rowsAndColumns[1]));
                seating.setColumns(rowsAndColumns[2]);

                // Add this seating type to the list
                seatingList.add(seating);
            }
        }
        return seatingList;
    }


    public static String getSeatType(String seatNumber, String seatingPlan)
    {
        int businessEndRowNonFinal = 0;
        List<SeatingTypeDTO> seatingList = decodeSeatingType(seatingPlan);
        for (SeatingTypeDTO seating : seatingList) {
            if ("business".equals(seating.getType())) {
                businessEndRowNonFinal = seating.getEndRow();
                break;
            }
        }
        final int businessEndRow = businessEndRowNonFinal;
        int rowNumber = Integer.parseInt(seatNumber.substring(1));
        return rowNumber <= businessEndRow ? "business" : "economy";
    }

}
