package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguagePK;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinLanguageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinCrewLanguageRepositoryTests {

    @Autowired
    private CabinLanguageRepository cabinLanguageRepository;

    @Test
    public void CabinLanguageRepository_FindAttendantLanguageEntitiesByAttendantLanguagePK_AttendantId() {
        AttendantLanguagePK attendantLanguagePK = new AttendantLanguagePK(1, "English");

        AttendantLanguageEntity attendantLanguageEntity = new AttendantLanguageEntity(attendantLanguagePK);

        cabinLanguageRepository.save(attendantLanguageEntity);

        List<AttendantLanguageEntity> attendantLanguages = cabinLanguageRepository.findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(1);

        Assertions.assertThat(attendantLanguages).isNotEmpty();
        Assertions.assertThat(attendantLanguages.size()).isEqualTo(1);
        Assertions.assertThat(attendantLanguages.get(0).getAttendantLanguagePK().getAttendantId()).isEqualTo(1);
        Assertions.assertThat(attendantLanguages.get(0).getAttendantLanguagePK().getLanguage()).isEqualTo("English");
    }
}
