package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.FlightEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentPK;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotAssignmentRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Util.FlightDateChecker;
import com.su.FlightScheduler.Util.SeatIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<UserDataDTO> getAvailablePilotsForFlight(String flightNumber) {
        if (!flightRepository.existsById(flightNumber))
        {
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        FlightEntity flight = flightRepository.findById(flightNumber).get();
        LocalDateTime departureTime = flight.getDepartureDateTime();
        String sourceCity = flight.getSourceAirport().getCity();
        int flightRange = flight.getFlightRange();
        //find the currently assigned pilot list
        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber);

        //find the needed role
        int size = pilotAssignmentEntityList.size();
        int seniorSize = flight.getPlane().getVehicleType().getSeniorPilotCapacity();
        int juniorSize = flight.getPlane().getVehicleType().getJuniorPilotCapacity();
        int traineeSize = flight.getPlane().getVehicleType().getTraineePilotCapacity();


        List<PilotEntity> availablePilotList = new ArrayList<>();

        if (size < seniorSize)  //find available senior pilot
        {
            //find all senior pilots that can fly flightRange
            List<PilotEntity> pilotEntityList = pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual("Senior", flightRange);
            availablePilotList = findAvailablePilotsFromGivenListAndFlight(pilotEntityList, flight);
        }
        else if (size < seniorSize + juniorSize)    //find available junior pilot
        {
            //find all junior pilots that can fly flightRange
            List<PilotEntity> pilotEntityList = pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual("Junior", flightRange);
            availablePilotList = findAvailablePilotsFromGivenListAndFlight(pilotEntityList, flight);

        }
        else if (size < seniorSize + juniorSize + traineeSize)  //find available trainee pilot
        {
            //find all trainee pilots that can fly flight range
            List<PilotEntity> pilotEntityList = pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual("Trainee", flightRange);
            availablePilotList = findAvailablePilotsFromGivenListAndFlight(pilotEntityList, flight);
        }
        else    //flight pilot capacity is full
        {
            throw new RuntimeException("Pilot capacity of flight with id: " + flightNumber + " is full!");
        }

        List<UserDataDTO> userDataDTOList = UserDataDTOFactory.create_available_pilot_list(availablePilotList);

        return userDataDTOList;
    }

    @Override
    public UserDataDTO assignPilotToFlight(String flightNumber, int pilotId) {
        if (!flightRepository.existsById(flightNumber))
        {
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        if (!pilotRepository.existsById(pilotId))
        {
            throw new RuntimeException("Pilot with id: " + pilotId + " does not exist!");
        }
        //find the flight
        FlightEntity flightEntity = flightRepository.findById(flightNumber).get();
        //find the current assignment list
        List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber);
        //find the to be assigned pilot
        PilotEntity pilot = pilotRepository.findById(pilotId).get();
        //primary key is set for PilotAssignmentEntity
        PilotAssignmentPK pilotAssignmentPK = new PilotAssignmentPK(pilotId, flightNumber);
        //calculate sizes
        int size = pilotAssignmentEntityList.size();
        int seniorSize = flightEntity.getPlane().getVehicleType().getSeniorPilotCapacity();
        int juniorSize = flightEntity.getPlane().getVehicleType().getJuniorPilotCapacity();
        int traineeSize = flightEntity.getPlane().getVehicleType().getTraineePilotCapacity();


        PilotAssignmentEntity savedPilotAssignmentEntity;
        if (size < seniorSize)  //assign senior pilot
        {
            //find the seat
            String newSeat;
            if (size == 0)
            {
                newSeat = "0A";
            }
            else
            {
                String lastSeat = pilotAssignmentEntityList.get(size-1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            //create PilotAssignmentEntity
            PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(pilotAssignmentPK, "Senior", newSeat,1);
            //set pilot and flight field
            pilotAssignmentEntity.setPilot(pilot);
            pilotAssignmentEntity.setFlight(flightEntity);
            //save PilotAssignmentEntity
            savedPilotAssignmentEntity = pilotAssignmentRepository.save(pilotAssignmentEntity);
        }
        else if (size < seniorSize + juniorSize)    //assign a junior pilot
        {
            //find the seat
            String newSeat;
            if (size == 0)
            {
                newSeat = "1A";
            }
            else
            {
                String lastSeat = pilotAssignmentEntityList.get(size-1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            //create PilotAssignmentEntity
            PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(pilotAssignmentPK, "Junior", "1A",1);
            //set pilot and flight field
            pilotAssignmentEntity.setPilot(pilot);
            pilotAssignmentEntity.setFlight(flightEntity);
            //save PilotAssignmentEntity
            savedPilotAssignmentEntity = pilotAssignmentRepository.save(pilotAssignmentEntity);
        }
        else if (size < seniorSize + juniorSize + traineeSize)  //assign a trainee pilot
        {
            //find the seat
            String newSeat;
            if (size == 0)
            {
                newSeat = "2A";
            }
            else
            {
                String lastSeat = pilotAssignmentEntityList.get(size-1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            //create PilotAssignmentEntity
            PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(pilotAssignmentPK, "Trainee", "1A",1);
            //set pilot and flight field
            pilotAssignmentEntity.setPilot(pilot);
            pilotAssignmentEntity.setFlight(flightEntity);
            //save PilotAssignmentEntity
            savedPilotAssignmentEntity = pilotAssignmentRepository.save(pilotAssignmentEntity);
        }
        else
        {
            throw new RuntimeException("Pilot capacity of flight with id: " + flightNumber + " is full!");
        }

        UserDataDTO userDataDTO = new UserDataDTO(savedPilotAssignmentEntity);
        return userDataDTO;
    }

    @Override
    public List<UserDataDTO> getPilotsOfFlight(String flightNumber) {
        if (flightRepository.existsById(flightNumber))  //flight exists in the repository
        {
            //get every assignment
            List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_FlightNumber(flightNumber);
            List<UserDataDTO> userDataDTOList = new ArrayList<>();
            for (PilotAssignmentEntity pilotAssignmentEntity : pilotAssignmentEntityList)
            {
                UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_data_with_given_flight(pilotAssignmentEntity, flightNumber);
                userDataDTOList.add(userDataDTO);
            }
            return userDataDTOList;
        }
        else
        {
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
    }


    private List<PilotEntity> findAvailablePilotsFromGivenListAndFlight(List<PilotEntity> pilotEntityList, FlightEntity flight)
    {
        List<PilotEntity> availablePilotList = new ArrayList<>();
        for (PilotEntity pilotCandidate : pilotEntityList)
        {
            //get flights of the current pilot
            List<PilotAssignmentEntity> currentPilotAssignmentList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotCandidate.getPilotId());

            if (currentPilotAssignmentList.size() == 0) //pilot has no flights, pilot can be assigned
            {
                availablePilotList.add(pilotCandidate);
            }
            else
            {
                boolean isAvailable = true;
                //for each assignment
                for (PilotAssignmentEntity currentPilotAssignment : currentPilotAssignmentList)
                {
                    if (!FlightDateChecker.flightDoesNotOverlap(currentPilotAssignment.getFlight(), flight))
                    {
                        isAvailable = false;    //one overlap makes the pilot unavailable
                        break;
                    }
                }
                if (isAvailable)    //pilot is available if none of the assignments overlap
                {
                    availablePilotList.add(pilotCandidate); //add to available list
                }
            }
        }
        return availablePilotList;
    }
}
