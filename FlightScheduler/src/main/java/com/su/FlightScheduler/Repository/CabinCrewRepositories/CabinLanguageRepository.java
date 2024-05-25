package com.su.FlightScheduler.Repository.CabinCrewRepositories;

import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguagePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//TESTING: this one function should be tested
public interface CabinLanguageRepository extends JpaRepository<AttendantLanguageEntity, AttendantLanguagePK>{

    public List<AttendantLanguageEntity> findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(int attendantId);
}
