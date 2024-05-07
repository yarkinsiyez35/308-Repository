package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PilotLanguageRepository extends JpaRepository <PilotLanguageEntity, PilotLanguagePK> {

        public List<PilotLanguageEntity> findPilotLanguageEntitiesByPilotLanguagePK_PilotId(int pilotId);
}
