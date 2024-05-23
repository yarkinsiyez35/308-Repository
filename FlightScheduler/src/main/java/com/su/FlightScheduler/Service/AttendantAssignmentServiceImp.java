package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinAssignmentRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            UserDataDTO userDataDTO = UserDataDTOFactory.create_cabincrew_data_with_flight_list(cabinCrewAssignmentsEntityList);


            return userDataDTO;
        }
        else{
            throw new RuntimeException("Attendant with id: " + attendantId + " does not exist!");
        }
    }

    @Override
    public UserDataDTO assignAttendantToFlight(String flightNumber, int attendantId) {
        return null;
    }

    @Override
    public List<UserDataDTO> getAttendantsOfFlight(String flightNumber) {

        if (flightRepository.existsById(flightNumber)){

            List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList = cabinAssignmentRepository.findCabinCrewAssignmentsEntitiesByCabinCrewAssignmentsPK_FlightNumber(flightNumber);
            List<UserDataDTO> userDataDTOList = new ArrayList<>();
            for (CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity: cabinCrewAssignmentsEntityList){

                UserDataDTO userDataDTO = UserDataDTOFactory.create_cabincrew_data_with_flight_list(cabinCrewAssignmentsEntityList);
                userDataDTOList.add(userDataDTO);
            }
            return userDataDTOList;
        }
        else{
            throw new RuntimeException("Cabin Crew member with id: " + flightNumber + " does not exist!");
        }
    }
}
