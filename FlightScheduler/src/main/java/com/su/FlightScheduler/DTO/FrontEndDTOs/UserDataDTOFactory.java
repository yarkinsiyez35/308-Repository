package com.su.FlightScheduler.DTO.FrontEndDTOs;

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
}
