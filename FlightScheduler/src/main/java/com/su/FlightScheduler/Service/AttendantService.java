package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import java.util.List;

public interface AttendantService {

    void addAttendant(CabinCrewEntity attendant);

    CabinCrewEntity findAttendantById(int id);

    boolean updateAttendant(int id, CabinCrewEntity updatedAttendant);

    boolean deleteAttendant(int id);

    List<CabinCrewEntity> getAllAttendants();
}
