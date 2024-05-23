package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguagePK;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinLanguageRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class CabinLanguageRepositoryTests {
    @Autowired
    private CabinCrewRepository cabinCrewRepository;

    @Autowired
    private CabinLanguageRepository cabinLanguageRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void CabinLanguageRepository_SaveLanguage() {
        // Save a CabinCrewEntity first
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);

        AttendantLanguageEntity attendantLanguageEntity = new AttendantLanguageEntity(new AttendantLanguagePK(1, "English"));
        AttendantLanguageEntity savedAttendantLanguageEntity = cabinLanguageRepository.save(attendantLanguageEntity);

        Assertions.assertThat(savedAttendantLanguageEntity).isNotNull();
        Assertions.assertThat(savedAttendantLanguageEntity.getAttendantLanguagePK().getAttendantId()).isEqualTo(1);
    }

    @Test
    public void CabinLanguageRepository_SaveLanguages() {
        // Save a CabinCrewEntity first
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);

        // Save multiple AttendantLanguageEntities
        AttendantLanguageEntity languageEntity1 = new AttendantLanguageEntity(new AttendantLanguagePK(1, "English"));
        AttendantLanguageEntity languageEntity2 = new AttendantLanguageEntity(new AttendantLanguagePK(1, "German"));

        List<AttendantLanguageEntity> languageEntityList = new ArrayList<>();
        languageEntityList.add(languageEntity1);
        languageEntityList.add(languageEntity2);

        List<AttendantLanguageEntity> savedLanguageEntityList = cabinLanguageRepository.saveAll(languageEntityList);

        assertThat(savedLanguageEntityList).isNotNull();
        assertThat(savedLanguageEntityList.size()).isEqualTo(2);
    }

    @Test
    public void CabinLanguageRepository_FindLanguagesById() {
        // Save a CabinCrewEntity first and then languages
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        AttendantLanguageEntity languageEntity1 = new AttendantLanguageEntity(new AttendantLanguagePK(1, "English"));
        AttendantLanguageEntity languageEntity2 = new AttendantLanguageEntity(new AttendantLanguagePK(1, "German"));
        cabinLanguageRepository.saveAll(List.of(languageEntity1, languageEntity2));

        List<AttendantLanguageEntity> foundLanguages = cabinLanguageRepository.findAttendantLanguageEntitiesByAttendantLanguagePK_attendantId(1);

        assertThat(foundLanguages).isNotNull();
        assertThat(foundLanguages.size()).isEqualTo(2);
    }

    @Test
    public void CabinLanguagesRepository_DeleteCabinCrewById() {
        // Save a CabinCrewEntity and a language
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        AttendantLanguageEntity languageEntity = new AttendantLanguageEntity(new AttendantLanguagePK(1, "English"));
        cabinLanguageRepository.save(languageEntity);

        entityManager.clear();

        // Deleting the cabin crew should ideally cascade to delete languages or be handled separately
        cabinCrewRepository.deleteById(1);

        List<AttendantLanguageEntity> remainingLanguages = cabinLanguageRepository.findAll();
        assertThat(remainingLanguages.size()).isEqualTo(0);
    }
}