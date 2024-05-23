package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PilotFlightAssignmentServiceImp implements PilotFlightAssignmentService{

    private final PilotRepository pilotRepository;
    private final PilotAssignmentRepository pilotAssignmentRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public PilotFlightAssignmentServiceImp(PilotRepository pilotRepository, PilotAssignmentRepository pilotAssignmentRepository, FlightRepository flightRepository) {
        this.pilotRepository = pilotRepository;
        this.pilotAssignmentRepository = pilotAssignmentRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public UserDataDTO getFlightsOfPilot(int pilotId) {

        if (pilotRepository.existsById(pilotId))    //pilot exists in the repository
        {
            //find every PilotAssignmentEntity
            List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId);
            UserDataDTO userDataDTO = new UserDataDTO(pilotAssignmentEntityList);
            return userDataDTO;
        }
        else
        {
            throw new RuntimeException("Pilot with id: " + pilotId + " does not exist!");
        }
    }

    @Override
    public FlightEntity assignPilotToFlight(String flightNumber, int pilotId, String role) {
        return null;
    }

    @Override
    public List<PilotEntity> getPilotsOfFlight(String flightNumber) {
        return null;
    }
}
