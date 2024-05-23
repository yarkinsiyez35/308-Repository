package com.su.FlightScheduler.DTO.FrontEndDTOs;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;
import com.su.FlightScheduler.Entity.PilotEntity;

import java.util.ArrayList;
import java.util.List;

public class UserDataDTOFactory {

    public static UserDataDTO create_pilot_data_with_given_flight(PilotAssignmentEntity pilotAssignmentEntity, String flightNumber)
    {
        UserDataDTO userDataDTO = new UserDataDTO(pilotAssignmentEntity);
        List<UserFlightDataDTO> userFlightDataDTOList = userDataDTO.getFlights();
        List<UserFlightDataDTO> userFlightDataDTOListWithGivenFlightNumber = new ArrayList<>();
        for (UserFlightDataDTO userFlightDataDTO : userFlightDataDTOList)
        {
            if (userFlightDataDTO.getFlightData().getFlightId().equals(flightNumber))
            {
                userFlightDataDTOListWithGivenFlightNumber.add(userFlightDataDTO);
                break;
            }
        }
        userDataDTO.setFlights(userFlightDataDTOListWithGivenFlightNumber);
        return userDataDTO;
    }


    public static UserDataDTO create_pilot_data_with_no_flight_from_pilot_entity(PilotEntity pilotEntity)
    {
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(pilotEntity.getEmail());
        userDataDTO.setPassword(pilotEntity.getPassword());
        userDataDTO.setName(pilotEntity.getFirstName());
        userDataDTO.setSurname(pilotEntity.getSurname());
        userDataDTO.setId(Integer.toString(pilotEntity.getPilotId()));
        userDataDTO.setAge(pilotEntity.getAge());
        userDataDTO.setGender(pilotEntity.getGender());
        userDataDTO.setNationality(pilotEntity.getNationality());
        userDataDTO.setFlights(null);
        return userDataDTO;
    }


    public static List<UserDataDTO> create_available_pilot_list(List<PilotEntity> pilotEntityList)
    {
        List<UserDataDTO> userDataDTOList = new ArrayList<>();
        for (PilotEntity pilotEntity : pilotEntityList)
        {
            UserDataDTO userDataDTO = create_pilot_data_with_no_flight_from_pilot_entity(pilotEntity);
            userDataDTOList.add(userDataDTO);
        }
        return userDataDTOList;

    public static UserDataDTO create_cabincrew_data_with_flight_list(List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList)
    {
        UserDataDTO userDataDTO = new UserDataDTO();
        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = cabinCrewAssignmentsEntityList.get(0);
        userDataDTO.setEmail(cabinCrewAssignmentsEntity.getCabinCrew().getEmail());
        userDataDTO.setPassword(cabinCrewAssignmentsEntity.getCabinCrew().getPassword());
        userDataDTO.setName(cabinCrewAssignmentsEntity.getCabinCrew().getFirstName());
        userDataDTO.setSurname(cabinCrewAssignmentsEntity.getCabinCrew().getSurname());
        userDataDTO.setId(Integer.toString(cabinCrewAssignmentsEntity.getCabinCrew().getAttendantId()));
        userDataDTO.setAge( cabinCrewAssignmentsEntity.getCabinCrew().getAge());
        userDataDTO.setNationality( cabinCrewAssignmentsEntity.getCabinCrew().getNationality());
        userDataDTO.setUserType("CabinCrew");
        List<UserFlightDataDTO> userFlightDataDTOList = new ArrayList<>();
        for (CabinCrewAssignmentsEntity cabinCrewAssignment : cabinCrewAssignmentsEntityList){
            userFlightDataDTOList.add(new UserFlightDataDTO(cabinCrewAssignment.getFlight(), cabinCrewAssignment));
        }
        userDataDTO.setFlights(userFlightDataDTOList);
        return userDataDTO;

    }
}
