package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.CabinCrewEntites.*;
import com.su.FlightScheduler.Entity.FlightEntitites.CityEntity;
import com.su.FlightScheduler.Entity.FlightEntitites.FlightEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentPK;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import com.su.FlightScheduler.Util.FlightDateChecker;
import com.su.FlightScheduler.Util.SeatIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TESTING: this service should be tested

@Service
public class AttendantAssignmentServiceImp implements AttendantAssignmentService{

    private final CabinAssignmentRepository cabinAssignmentRepository;
    private final FlightRepository flightRepository;
    private final CabinCrewRepository cabinCrewRepository;

    @Autowired
    public AttendantAssignmentServiceImp(CabinAssignmentRepository cabinAssignmentRepository, FlightRepository flightRepository, CabinCrewRepository cabinCrewRepository) {
        this.cabinAssignmentRepository = cabinAssignmentRepository;
        this.flightRepository = flightRepository;
        this.cabinCrewRepository = cabinCrewRepository;
    }



    @Override
    public UserDataDTO getFlightsOfAttendant(int attendantId) {

        Optional<CabinCrewEntity> cabinCrewEntity = cabinCrewRepository.findById(attendantId);
        if(cabinCrewEntity.isPresent()) {

            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(attendantId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_flight_list(cabinCrewAssignmentsEntityList, cabinCrewEntity.get());

            return userDataDTO;
        }
        else{
            throw new RuntimeException("Attendant with id: " + attendantId + " does not exist!");
        }
    }

    @Override
    public UserDataDTO assignAttendantToFlight(String flightNumber, int attendantId) {

        if(!flightRepository.existsById(flightNumber)){
            throw new RuntimeException("Flight with id: " + flightNumber + "does not exist!");
        }
        if(!cabinCrewRepository.existsById(attendantId)){
            throw new RuntimeException("Cabin crew member with id: " + attendantId + "does not exist!");
        }

        FlightEntity flightEntity = flightRepository.findById(flightNumber).get();

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);

        CabinCrewEntity cabin = cabinCrewRepository.findById(attendantId).get();

        CabinCrewAssignmentsPK cabinCrewAssignmentsPK = new CabinCrewAssignmentsPK(attendantId,flightNumber);

        int size = cabinCrewAssignmentsEntityList.size();
        int seniorSize = flightEntity.getPlane().getVehicleType().getSeniorAttendeeCapacity();
        int juniorSize = flightEntity.getPlane().getVehicleType().getJuniorAttendeeCapacity();
        int chefSize = flightEntity.getPlane().getVehicleType().getChefAttendeeCapacity();

        CabinCrewAssignmentsEntity savedCabinCrewAssignmentsEntity;
        if (size < seniorSize){

            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity;
            String newSeat;
            if (size == 0)
            {
                newSeat = "0A";
                cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Chief", newSeat, 1);
            }
            else {
                String lastSeat = SeatIncrementer.findLastCabinCrewSeat(cabinCrewAssignmentsEntityList);
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
                cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Senior", newSeat, 1);
            }

            cabinCrewAssignmentsEntity.setCabinCrew(cabin);
            cabinCrewAssignmentsEntity.setFlight(flightEntity);
            savedCabinCrewAssignmentsEntity = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
        }
        else if(size < seniorSize + juniorSize) {

            String newSeat;
            if (size == seniorSize) {
                newSeat = "1A";
            } else {
                String lastSeat = SeatIncrementer.findLastCabinCrewSeat(cabinCrewAssignmentsEntityList);
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Junior", newSeat, 1);
            cabinCrewAssignmentsEntity.setCabinCrew(cabin);
            cabinCrewAssignmentsEntity.setFlight(flightEntity);
            savedCabinCrewAssignmentsEntity = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
        }
        else if(size < seniorSize + juniorSize + chefSize){

            String newSeat;
            if (size == seniorSize + juniorSize){
                newSeat = "2A";
                //set recipe here
                String dish = cabin.getRecipes().get(0).getDishRecipePK().getRecipe();
                flightEntity.setStandardMenu(dish);

                flightEntity = flightRepository.save(flightEntity);
            }
            else{
                String lastSeat = SeatIncrementer.findLastCabinCrewSeat(cabinCrewAssignmentsEntityList);
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
                //set recipe here
                String dish = cabin.getRecipes().get(0).getDishRecipePK().getRecipe();
                flightEntity.setStandardMenu(flightEntity.getStandardMenu() + ", " + dish);
                flightEntity = flightRepository.save(flightEntity);
            }
            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Chef", newSeat, 1);
            cabinCrewAssignmentsEntity.setCabinCrew(cabin);
            cabinCrewAssignmentsEntity.setFlight(flightEntity);
            savedCabinCrewAssignmentsEntity = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
        }
        else{
            throw new RuntimeException("Cabin Crew capacity of flight with id: " + flightNumber + " is full!");
        }

        UserDataDTO userDataDTO = new UserDataDTO(savedCabinCrewAssignmentsEntity);
        return  userDataDTO;
    }

    @Override
    public UserDataDTO assignAttendantToFlightWithGivenRoleAndSeat(String flightNumber, String role, String seatNumber) throws RuntimeException {
        Optional<FlightEntity> flightEntity = flightRepository.findById(flightNumber);
        if (flightEntity.isEmpty())
        {
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        //find flight range
        int flightRange = flightEntity.get().getFlightRange();
        //find candidate cabin crew
        List<CabinCrewEntity> candidateCabinCrewList =  cabinCrewRepository.findCabinCrewEntityBySeniority(role);
        //find available cabin crew
        List<CabinCrewEntity> availableCabinCrewList = findAvailableAttendantsFromGivenListAndFlight(candidateCabinCrewList,flightEntity.get());

        if (!availableCabinCrewList.isEmpty())
        {
            //first available cabin crew will be assigned
            CabinCrewEntity toBeAssignedCabinCrew = availableCabinCrewList.get(0);
            //create the entity
            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(new CabinCrewAssignmentsPK(toBeAssignedCabinCrew.getAttendantId(),flightNumber), role, seatNumber, 1);
            //if chef, update the recipe



            cabinCrewAssignmentsEntity.setFlight(flightEntity.get());
            cabinCrewAssignmentsEntity.setCabinCrew(toBeAssignedCabinCrew);
            CabinCrewAssignmentsEntity savedCabinCrewAssignment = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_assignment(savedCabinCrewAssignment);
            return userDataDTO;
        }
        else
        {
            throw new RuntimeException("Cannot add a new cabin crew to flight with id: " + flightNumber +"!");
        }

    }

    @Override
    public List<UserDataDTO> getAttendantsOfFlight(String flightNumber) {

        if (flightRepository.existsById(flightNumber)){

            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
            List<UserDataDTO> userDataDTOList = new ArrayList<>();
            for (CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity: cabinCrewAssignmentsEntityList){

                UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_assignment(cabinCrewAssignmentsEntity);
                userDataDTOList.add(userDataDTO);
            }
            return userDataDTOList;
        }
        else{
            throw new RuntimeException("Cabin Crew member with id: " + flightNumber + " does not exist!");
        }
    }

    @Override
    public UserDataDTO removeAttendantFromFlight(String flightNumber, int attendantId) {
        Optional<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntity = cabinAssignmentRepository.findById(new CabinCrewAssignmentsPK(attendantId, flightNumber));
        if (cabinCrewAssignmentsEntity.isEmpty())
        {
            throw new RuntimeException("Cannot remove attendant with id: " + attendantId + " from flight with id: " + flightNumber + "!");
        }
        try
        {
            //call another function to assign
            assignAttendantToFlightWithGivenRoleAndSeat(flightNumber, cabinCrewAssignmentsEntity.get().getAssignmentRole(), cabinCrewAssignmentsEntity.get().getSeatNumber());
            //delete the assignment from the repository
            cabinAssignmentRepository.deleteById(new CabinCrewAssignmentsPK(attendantId, flightNumber));
            //return the information of the removed pilot
            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(attendantId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_flight_list(cabinCrewAssignmentsEntityList, cabinCrewAssignmentsEntity.get().getCabinCrew());
            return userDataDTO;
        }
        catch(RuntimeException e)
        {
            throw new RuntimeException("Cannot remove attendant with id: " + attendantId + " from flight with id: " + flightNumber + "because " + e.getMessage());
        }
    }

    @Override
    public List<UserDataDTO> getAvailableAttendantsForFlight(String flightNumber){
        if (!flightRepository.existsById(flightNumber)){
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        FlightEntity flight = flightRepository.findById(flightNumber).get();
        LocalDateTime departureTime = flight.getDepartureDateTime();
        CityEntity sourceCityEntity = flight.getSourceAirport().getCity();
        String sourceCity = sourceCityEntity.getCityName();
        int flightRange = flight.getFlightRange();

        List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);

        int size = cabinCrewAssignmentsEntityList.size();
        int seniorSize = flight.getPlane().getVehicleType().getSeniorAttendeeCapacity();
        int juniorSize = flight.getPlane().getVehicleType().getJuniorAttendeeCapacity();
        int chefSize = flight.getPlane().getVehicleType().getChefAttendeeCapacity();

        List<CabinCrewEntity> availableAttendantList = new ArrayList<>();

        if(size < seniorSize){

            List<CabinCrewEntity> cabinCrewEntityList = cabinCrewRepository.findCabinCrewEntityBySeniority("Senior");
            availableAttendantList = findAvailableAttendantsFromGivenListAndFlight(cabinCrewEntityList, flight);
        }
        else if (size < seniorSize + juniorSize) {

            List<CabinCrewEntity> cabinCrewEntityList = cabinCrewRepository.findCabinCrewEntityBySeniority("Junior");
            availableAttendantList = findAvailableAttendantsFromGivenListAndFlight(cabinCrewEntityList, flight);
        }
        else if(size < seniorSize + juniorSize + chefSize){

            List<CabinCrewEntity> cabinCrewEntityList = cabinCrewRepository.findCabinCrewEntityBySeniority("Chef");
            availableAttendantList = findAvailableAttendantsFromGivenListAndFlight(cabinCrewEntityList, flight);
            
        }
        else{
            throw new RuntimeException("Cabin Crew capacity of flight with id: " + flightNumber + " is full!");
        }

        List<UserDataDTO> userDataDTOList = UserDataDTOFactory.create_available_attendant_list(availableAttendantList); //create the method
        return userDataDTOList;
    }


    private List<CabinCrewEntity> findAvailableAttendantsFromGivenListAndFlight(List<CabinCrewEntity> cabinCrewEntityList, FlightEntity flight){

        List<CabinCrewEntity> availableAttendantList = new ArrayList<>();

        for (CabinCrewEntity cabinCandidate : cabinCrewEntityList){

            List<CabinCrewAssignmentsEntity> currentCabinCrewAssignmentList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(cabinCandidate.getAttendantId());

            if (currentCabinCrewAssignmentList.size() == 0){
                availableAttendantList.add(cabinCandidate);
            }
            else{

                boolean isAvailable = true;

                for (CabinCrewAssignmentsEntity currentCabinCrewAssignment : currentCabinCrewAssignmentList){
                    if (!FlightDateChecker.flightDoesNotOverlap(currentCabinCrewAssignment.getFlight(), flight)){
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    availableAttendantList.add(cabinCandidate);
                }
            }
        }
        return availableAttendantList;
    }
}
