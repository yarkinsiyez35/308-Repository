package com.su.FlightScheduler.DTO.FrontEndDTOs;


import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewAssignmentsEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
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

    public static UserDataDTO create_pilot_data_with_flight_list(List<PilotAssignmentEntity> pilotAssignmentEntityList, PilotEntity pilotEntity)
    {
        if (pilotAssignmentEntityList.isEmpty())
        {
            UserDataDTO userDataDTO = create_pilot_data_with_no_flight_from_pilot_entity(pilotEntity);
            return userDataDTO;
        }
        else
        {
            PilotAssignmentEntity pilotAssignmentEntity = pilotAssignmentEntityList.get(0);
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setEmail(pilotAssignmentEntity.getPilot().getEmail());
            userDataDTO.setPassword(pilotAssignmentEntity.getPilot().getPassword());
            userDataDTO.setName(pilotAssignmentEntity.getPilot().getFirstName());
            userDataDTO.setSurname(pilotAssignmentEntity.getPilot().getSurname());
            userDataDTO.setId(Integer.toString(pilotAssignmentEntity.getPilot().getPilotId()));
            userDataDTO.setAge(pilotAssignmentEntity.getPilot().getAge());
            userDataDTO.setGender(pilotAssignmentEntity.getPilot().getGender());
            userDataDTO.setNationality(pilotAssignmentEntity.getPilot().getNationality());
            userDataDTO.setUserType("PilotCrew");
            List<UserFlightDataDTO> userFlightDataDTOList = new ArrayList<>();
            for (PilotAssignmentEntity pilotAssignment : pilotAssignmentEntityList)
            {
                userFlightDataDTOList.add(new UserFlightDataDTO(pilotAssignment.getFlight(), pilotAssignment));
            }
            userDataDTO.setFlights(userFlightDataDTOList);
            return userDataDTO;
        }

    }

    //this needs refactoring, list might be empty
    public static UserDataDTO create_passenger_data_with_given_flight(List<PassengerFlight> passengerFlightList) {
        UserDataDTO userDataDTO = new UserDataDTO();
        PassengerFlight passengerFlight = passengerFlightList.get(0);
        userDataDTO.setEmail(passengerFlight.getPassenger().getEmail());
        userDataDTO.setPassword(passengerFlight.getPassenger().getPassword());
        userDataDTO.setName(passengerFlight.getPassenger().getFirstName());
        userDataDTO.setSurname(passengerFlight.getPassenger().getSurname());
        userDataDTO.setId(Integer.toString(passengerFlight.getPassenger().getPassengerId()));
        userDataDTO.setAge(passengerFlight.getPassenger().getAge());
        userDataDTO.setNationality(passengerFlight.getPassenger().getNationality());
        userDataDTO.setUserType("Passenger");
        List<UserFlightDataDTO> userFlightDataDTOList = new ArrayList<>();
        for (PassengerFlight passengerFlight1 : passengerFlightList) {
            userFlightDataDTOList.add(new UserFlightDataDTO(passengerFlight1.getFlight(), passengerFlight1));
        }
        return userDataDTO;
    }

    public static UserDataDTO create_passenger_data_with_no_flight_from_passenger_entity(PassengerEntity passengerEntity)
    {
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(passengerEntity.getEmail());
        userDataDTO.setPassword(passengerEntity.getPassword());
        userDataDTO.setName(passengerEntity.getFirstName());
        userDataDTO.setSurname(passengerEntity.getSurname());
        userDataDTO.setId(Integer.toString(passengerEntity.getPassengerId()));
        userDataDTO.setAge(passengerEntity.getAge());
        userDataDTO.setNationality(passengerEntity.getNationality());
        userDataDTO.setUserType("Passenger");
        userDataDTO.setFlights(null);
        return userDataDTO;
    }

    public static UserDataDTO create_cabin_crew_data_with_no_flight_from_pilot_entity(CabinCrewEntity cabinCrewEntity){
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(cabinCrewEntity.getEmail());
        userDataDTO.setPassword(cabinCrewEntity.getPassword());
        userDataDTO.setName(cabinCrewEntity.getFirstName());
        userDataDTO.setSurname(cabinCrewEntity.getSurname());
        userDataDTO.setId(Integer.toString(cabinCrewEntity.getAttendantId()));
        userDataDTO.setAge(cabinCrewEntity.getAge());
        userDataDTO.setGender(cabinCrewEntity.getGender());
        userDataDTO.setNationality(cabinCrewEntity.getNationality());
        userDataDTO.setFlights(null);
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

    public static List<UserDataDTO> create_available_pilot_list(List<PilotEntity> pilotEntityList) {
        List<UserDataDTO> userDataDTOList = new ArrayList<>();
        for (PilotEntity pilotEntity : pilotEntityList) {
            UserDataDTO userDataDTO = create_pilot_data_with_no_flight_from_pilot_entity(pilotEntity);
            userDataDTOList.add(userDataDTO);
        }
        return userDataDTOList;
    }

    public static List<UserDataDTO> create_available_attendant_list(List<CabinCrewEntity> cabinCrewEntityList){
        List<UserDataDTO> userDataDTOList = new ArrayList<>();
        for(CabinCrewEntity cabinCrewEntity : cabinCrewEntityList){
            UserDataDTO userDataDTO = create_cabin_crew_data_with_no_flight_from_pilot_entity(cabinCrewEntity);
            userDataDTOList.add(userDataDTO);
        }
        return userDataDTOList;
    }

    //this needs refactoring, list might be empty
    public static UserDataDTO create_cabin_crew_data_with_flight_list(List<CabinCrewAssignmentsEntity> cabinCrewAssignmentsEntityList)
    {
        UserDataDTO userDataDTO = new UserDataDTO();
        CabinCrewAssignmentsEntity cabinCrewAssignmentsEntity = cabinCrewAssignmentsEntityList.get(0);
        userDataDTO.setEmail(cabinCrewAssignmentsEntity.getCabinCrew().getEmail());
        userDataDTO.setPassword(cabinCrewAssignmentsEntity.getCabinCrew().getPassword());
        userDataDTO.setName(cabinCrewAssignmentsEntity.getCabinCrew().getFirstName());
        userDataDTO.setSurname(cabinCrewAssignmentsEntity.getCabinCrew().getSurname());
        userDataDTO.setId(Integer.toString(cabinCrewAssignmentsEntity.getCabinCrew().getAttendantId()));
        userDataDTO.setAge( cabinCrewAssignmentsEntity.getCabinCrew().getAge());
        userDataDTO.setGender(cabinCrewAssignmentsEntity.getCabinCrew().getGender());
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
