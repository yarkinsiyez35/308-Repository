package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.AttendantLanguagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CabinLanguageRepository extends JpaRepository<AttendantLanguageEntity, AttendantLanguagePK>{

    public List<AttendantLanguageEntity> findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(int attendantId);
}
