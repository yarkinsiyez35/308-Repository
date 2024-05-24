package com.su.FlightScheduler.Util;


import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//this class will be used when looking for available pilots and cabin crew members
public class FlightDateChecker {

    public static boolean flightDoesNotOverlap(FlightEntity flight1, FlightEntity flight2)
    {
        LocalDateTime departure1 = flight1.getDepartureDateTime();
        LocalDateTime landing1 = flight1.getLandingDateTime();
        LocalDateTime departure2 = flight2.getDepartureDateTime();
        LocalDateTime landing2 = flight2.getLandingDateTime();

        // Check if flights are on the same day
        boolean sameDay = departure1.toLocalDate().isEqual(departure2.toLocalDate());

        if (!sameDay) {
            return true;
        }

        String destination1 = flight1.getDestinationAirport().getAirportCode();
        String source2 = flight2.getSourceAirport().getAirportCode();

        String destination2 = flight2.getDestinationAirport().getAirportCode();
        String source1 = flight1.getSourceAirport().getAirportCode();

        // Check if flight1 ends before flight2 starts and they are in the same location
        boolean firstCondition = landing1.isBefore(departure2) && destination1.equals(source2);

        // Check if flight2 ends before flight1 starts and they are in the same location
        boolean secondCondition = landing2.isBefore(departure1) && destination2.equals(source1);

        return firstCondition || secondCondition;
    }
}
