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
import java.util.Optional;

//TESTING: this service should be tested
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

        Optional<PilotEntity> pilotEntity = pilotRepository.findById(pilotId);

        if (pilotEntity.isPresent())    //pilot exists in the repository
        {
            //find every PilotAssignmentEntity
            List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_data_with_flight_list(pilotAssignmentEntityList,pilotEntity.get());
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
                String lastSeat = SeatIncrementer.findLastPilotSeat(pilotAssignmentEntityList);
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
            if (size == seniorSize)
            {
                newSeat = "1A";
            }
            else
            {
                String lastSeat = SeatIncrementer.findLastPilotSeat(pilotAssignmentEntityList);
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            //create PilotAssignmentEntity
            PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(pilotAssignmentPK, "Junior", newSeat,1);
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
            if (size == seniorSize + juniorSize)
            {
                newSeat = "2A";
            }
            else
            {
                String lastSeat = SeatIncrementer.findLastPilotSeat(pilotAssignmentEntityList);
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            //create PilotAssignmentEntity
            PilotAssignmentEntity pilotAssignmentEntity = new PilotAssignmentEntity(pilotAssignmentPK, "Trainee", newSeat,1);
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
        userDataDTO = UserDataDTOFactory.create_pilot_data_with_given_flight(savedPilotAssignmentEntity, flightNumber);
        return userDataDTO;
    }

    public UserDataDTO assignAPilotToFlightWithGivenRoleAndSeat(String flightNumber, String role, String seatNumber) {
        Optional<FlightEntity> flightEntity = flightRepository.findById(flightNumber);
        if (flightEntity.isEmpty())
        {
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        //find flight range
        int flightRange = flightEntity.get().getFlightRange();
        //find candidate pilots
        List<PilotEntity> candidatePilotList =  pilotRepository.findPilotEntityBySeniorityAndAllowedRangeGreaterThanEqual(role,flightRange);
        //find available pilots
        List<PilotEntity> availablePilotList = findAvailablePilotsFromGivenListAndFlight(candidatePilotList,flightEntity.get());


        if (!availablePilotList.isEmpty())
        {
            //first available pilot will be assigned
            PilotEntity toBeAssignedPilot = availablePilotList.get(0);
            //create the entity
            PilotAssignmentEntity pilotAssignment = new PilotAssignmentEntity(new PilotAssignmentPK(toBeAssignedPilot.getPilotId(),flightNumber), role, seatNumber,1);
            pilotAssignment.setFlight(flightEntity.get());
            pilotAssignment.setPilot(toBeAssignedPilot);
            PilotAssignmentEntity savedPilotAssignment = pilotAssignmentRepository.save(pilotAssignment);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_with_pilot_assignment_entity(savedPilotAssignment);

            return userDataDTO;
        }
        else
        {
            throw new RuntimeException("Cannot add a new pilot to flight with id: " + flightNumber +"!");
        }
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

    @Override
    public UserDataDTO removeFlightFromAPilot(String flightNumber, int pilotId) {
        //find the assignment
        Optional<PilotAssignmentEntity> pilotAssignmentEntity = pilotAssignmentRepository.findById(new PilotAssignmentPK(pilotId, flightNumber));
        if (pilotAssignmentEntity.isEmpty())    //throw exception if assignment does not exist
        {
            throw new RuntimeException("Cannot remove pilot with id: " + pilotId + " from flight with id: " + flightNumber + "!");
        }
        try
        {
            //call another function to assign
            assignAPilotToFlightWithGivenRoleAndSeat(flightNumber, pilotAssignmentEntity.get().getAssignmentRole(), pilotAssignmentEntity.get().getSeatNumber());
            //delete the assignment from the repository
            pilotAssignmentRepository.deleteById(new PilotAssignmentPK(pilotId, flightNumber));
            //return the information of the removed pilot
            List<PilotAssignmentEntity> pilotAssignmentEntityList = pilotAssignmentRepository.findAllByPilotAssignmentPK_PilotId(pilotId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_pilot_data_with_flight_list(pilotAssignmentEntityList, pilotAssignmentEntity.get().getPilot());
            return userDataDTO;
        }
        catch(RuntimeException e)
        {
            throw new RuntimeException("Cannot remove pilot with id: " + pilotId + " from flight with id: " + flightNumber + "because " + e.getMessage());
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
