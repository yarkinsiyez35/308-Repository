package com.su.FlightScheduler.DTO.FrontEndDTOs;

import com.su.FlightScheduler.Entity.PassengerFlight;
import com.su.FlightScheduler.Entity.PilotAssignmentEntity;

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

    public static UserDataDTO create_passenger_data_with_given_flight(List<PassengerFlight> passengerFlightList)
    {
        UserDataDTO userDataDTO = new UserDataDTO();
        PassengerFlight passengerFlight = passengerFlightList.get(0);
        userDataDTO.setEmail(passengerFlight.getPassenger().getEmail());
        userDataDTO.setPassword(passengerFlight.getPassenger().getPassword());
        userDataDTO.setName(passengerFlight.getPassenger().getFirstName());
        userDataDTO.setSurname(passengerFlight.getPassenger().getSurname());
        userDataDTO.setId(Integer.toString(passengerFlight.getPassenger().getPassengerId()));
        userDataDTO.setAge( passengerFlight.getPassenger().getAge());
        userDataDTO.setNationality( passengerFlight.getPassenger().getNationality());
        userDataDTO.setUserType("Passenger");
        List<UserFlightDataDTO> userFlightDataDTOList = new ArrayList<>();
        for (PassengerFlight passengerFlight1 : passengerFlightList){
            userFlightDataDTOList.add(new UserFlightDataDTO(passengerFlight1.getFlight(), passengerFlight1));
        }
        userDataDTO.setFlights(userFlightDataDTOList);
        return userDataDTO;
    }
}
