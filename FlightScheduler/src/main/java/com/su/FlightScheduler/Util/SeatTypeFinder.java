package com.su.FlightScheduler.Util;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class SeatTypeFinder {

    //this code is taken from FlightServiceImp
    public static List<SeatingTypeDTO> decodeSeatingType(String seatingPlan)
    {
        List<SeatingTypeDTO> seatingList = new ArrayList<>();
        String[] seatingClasses = seatingPlan.split("=");

        // Process Business Class
        if (seatingClasses.length > 0) {
            String businessSeating = seatingClasses[0];
            String[] businessComponents = businessSeating.split("\\*");
            String[] businessColumns = businessComponents[0].split("\\|");
            int businessRows = Integer.parseInt(businessComponents[1]);

            SeatingTypeDTO businessSeatingDTO = new SeatingTypeDTO();
            businessSeatingDTO.setType("business");
            businessSeatingDTO.setStartRow(1); // assuming business class starts at row 1
            businessSeatingDTO.setEndRow(businessRows);
            businessSeatingDTO.setColumns(String.join("-", businessColumns));

            seatingList.add(businessSeatingDTO);
        }

        // Process Economy Class
        if (seatingClasses.length > 1) {
            String economySeating = seatingClasses[1];
            String[] economyComponents = economySeating.split("\\*");
            String[] economyColumns = economyComponents[0].split("\\|");
            int economyRows = Integer.parseInt(economyComponents[1]);

            SeatingTypeDTO economySeatingDTO = new SeatingTypeDTO();
            economySeatingDTO.setType("economy");
            economySeatingDTO.setStartRow(1); // assuming economy class starts at row 1
            economySeatingDTO.setEndRow(economyRows);
            economySeatingDTO.setColumns(String.join("-", economyColumns));

            seatingList.add(economySeatingDTO);
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
