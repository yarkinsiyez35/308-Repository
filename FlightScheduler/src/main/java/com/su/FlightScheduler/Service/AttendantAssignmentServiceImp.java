package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsPK;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.FlightEntity;
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

        if(cabinCrewRepository.existsById(attendantId)) {

            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_AttendantId(attendantId);
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_flight_list(cabinCrewAssignmentsEntityList);


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

            String newSeat;
            if (size == 0){
                newSeat = "0A";
            }
            else {
                String lastSeat = cabinCrewAssignmentsEntityList.get(size-1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }

            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Senior", newSeat, 1);
            cabinCrewAssignmentsEntity.setCabinCrew(cabin);
            cabinCrewAssignmentsEntity.setFlight(flightEntity);
            savedCabinCrewAssignmentsEntity = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
        }
        else if(size < seniorSize + juniorSize) {

            String newSeat;
            if (size == 0) {
                newSeat = "1A";
            } else {
                String lastSeat = cabinCrewAssignmentsEntityList.get(size - 1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Junior", "1A", 1);
            cabinCrewAssignmentsEntity.setCabinCrew(cabin);
            cabinCrewAssignmentsEntity.setFlight(flightEntity);
            savedCabinCrewAssignmentsEntity = cabinAssignmentRepository.save(cabinCrewAssignmentsEntity);
        }
        else if(size < seniorSize + juniorSize + chefSize){

            String newSeat;
            if (size == 0){
                newSeat = "2A";
            }
            else{
                String lastSeat = cabinCrewAssignmentsEntityList.get(size - 1).getSeatNumber();
                newSeat = SeatIncrementer.incrementSeat(lastSeat);
            }
            CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = new CabinCrewAssignmentsEntity(cabinCrewAssignmentsPK, "Chef", "2A", 1);
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
    public List<UserDataDTO> getAttendantsOfFlight(String flightNumber) {

        if (flightRepository.existsById(flightNumber)){

            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
            List<UserDataDTO> userDataDTOList = new ArrayList<>();
            for (CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity: cabinCrewAssignmentsEntityList){

                UserDataDTO userDataDTO = UserDataDTOFactory.create_cabin_crew_data_with_flight_list(cabinCrewAssignmentsEntityList);
                userDataDTOList.add(userDataDTO);
            }
            return userDataDTOList;
        }
        else{
            throw new RuntimeException("Cabin Crew member with id: " + flightNumber + " does not exist!");
        }
    }

    @Override
    public List<UserDataDTO> getAvailableAttendantsForFlight(String flightNumber){
        if (!flightRepository.existsById(flightNumber)){
            throw new RuntimeException("Flight with id: " + flightNumber + " does not exist!");
        }
        FlightEntity flight = flightRepository.findById(flightNumber).get();
        LocalDateTime departureTime = flight.getDepartureDateTime();
        String sourceCity = flight.getSourceAirport().getCity();
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
