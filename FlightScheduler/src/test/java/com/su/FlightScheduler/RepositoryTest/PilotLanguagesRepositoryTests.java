package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotLanguageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PilotLanguagesRepositoryTests {

    @Autowired
    private PilotLanguageRepository pilotLanguageRepository;

    @Test
    public void PilotLanguageRepository_FindPilotLanguageEntitiesByPilotLanguagePK_PilotId() {
        PilotLanguagePK pilotLanguagePK1 = new PilotLanguagePK(1, "English");
        PilotLanguagePK pilotLanguagePK2 = new PilotLanguagePK(1, "French");

        PilotLanguageEntity pilotLanguageEntity1 = new PilotLanguageEntity(pilotLanguagePK1);
        PilotLanguageEntity pilotLanguageEntity2 = new PilotLanguageEntity(pilotLanguagePK2);

        pilotLanguageRepository.save(pilotLanguageEntity1);
        pilotLanguageRepository.save(pilotLanguageEntity2);

        List<PilotLanguageEntity> pilotLanguages = pilotLanguageRepository.findPilotLanguageEntitiesByPilotLanguagePK_PilotId(1);

        Assertions.assertThat(pilotLanguages).isNotEmpty();
        Assertions.assertThat(pilotLanguages.size()).isEqualTo(2);
        Assertions.assertThat(pilotLanguages.get(0).getPilotLanguagePK().getPilotId()).isEqualTo(1);
        Assertions.assertThat(pilotLanguages.get(0).getPilotLanguagePK().getLanguage()).isIn("English", "French");
        Assertions.assertThat(pilotLanguages.get(1).getPilotLanguagePK().getPilotId()).isEqualTo(1);
        Assertions.assertThat(pilotLanguages.get(1).getPilotLanguagePK().getLanguage()).isIn("English", "French");
    }
}

