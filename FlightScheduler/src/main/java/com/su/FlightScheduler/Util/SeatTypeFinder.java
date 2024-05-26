package com.su.FlightScheduler.Util;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class SeatTypeFinder {


    public static String decodeColumns(String[] columns) {
        StringBuilder decodedColumns = new StringBuilder();
        int columnIndex = 0;

        for (int i = 0; i < columns.length; i++) {
            int numSeats = Integer.parseInt(columns[i]);
            char startColumn = (char) ('A' + columnIndex);
            char endColumn = (char) (startColumn + numSeats - 1);
            if (decodedColumns.length() > 0) {
                decodedColumns.append('/');
            }
            decodedColumns.append(startColumn).append('-').append(endColumn);
            columnIndex += numSeats;
        }

        return decodedColumns.toString();
    }


    //this code is taken from FlightServiceImp
    public static List<SeatingTypeDTO> decodeSeatingType(String seatingPlan)
    {
        List<SeatingTypeDTO> seatingList = new ArrayList<>();

        // Split the encoded seating plan into business and economy seating types
        String[] seatingClasses = seatingPlan.split("=");

        int currentRow = 1; // initialize the current row counter

        // Process each seating class (e.g., business, economy)
        for (String seatingClass : seatingClasses) {
            String[] components = seatingClass.split("\\*");
            String[] columnGroups = components[0].split("\\|");
            int rows = Integer.parseInt(components[1]);

            SeatingTypeDTO seatingTypeDTO = new SeatingTypeDTO();
            seatingTypeDTO.setType(currentRow == 1 ? "business" : "economy");
            seatingTypeDTO.setStartRow(currentRow);
            seatingTypeDTO.setEndRow(currentRow + rows - 1);
            seatingTypeDTO.setColumns(decodeColumns(columnGroups));

            seatingList.add(seatingTypeDTO);

            currentRow += rows; // update the current row counter
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
        int rowNumber = Integer.parseInt(seatNumber.replaceAll("[^0-9]", ""));
        return rowNumber <= businessEndRow ? "business" : "economy";
    }

}
