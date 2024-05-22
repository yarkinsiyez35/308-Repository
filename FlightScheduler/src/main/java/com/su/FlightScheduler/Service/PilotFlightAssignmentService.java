package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.FlightEntity;

public interface PilotFlightAssignmentService {


    public FlightEntity getFlightsOfPilot(int pilotId);


    public void assignPilotToFlight(String flightNumber, int pilotId, String role);



}
