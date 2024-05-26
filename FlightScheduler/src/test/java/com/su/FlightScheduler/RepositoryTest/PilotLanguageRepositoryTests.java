package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;
import com.su.FlightScheduler.Entity.PilotLanguagePK;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotLanguageRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PilotLanguageRepositoryTests {
    @Autowired
    private PilotRepository pilotRepository;
    @Autowired
    private PilotLanguageRepository pilotLanguageRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void PilotLanguageRepository_SaveLanguage()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);
        PilotLanguageEntity pilotLanguageEntity = new PilotLanguageEntity(new PilotLanguagePK(1, "English"));

        PilotLanguageEntity savedPilotLanguageEntity = pilotLanguageRepository.save(pilotLanguageEntity);

        Assertions.assertThat(savedPilotLanguageEntity).isNotNull();
        Assertions.assertThat(savedPilotLanguageEntity.getPilotLanguagePK().getPilotId()).isEqualTo(1);
    }

    @Test
    public void PilotLanguageRepository_SaveLanguages()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);

        PilotLanguageEntity pilotLanguageEntity1 = new PilotLanguageEntity((new PilotLanguagePK(1, "English")));
        PilotLanguageEntity pilotLanguageEntity2 = new PilotLanguageEntity((new PilotLanguagePK(1, "German")));

        List<PilotLanguageEntity> pilotLanguageEntityList = new ArrayList<>();
        pilotLanguageEntityList.add(pilotLanguageEntity1);
        pilotLanguageEntityList.add(pilotLanguageEntity2);

        List<PilotLanguageEntity> savedPilotLanguageEntityList = pilotLanguageRepository.saveAll(pilotLanguageEntityList);

        Assertions.assertThat(savedPilotLanguageEntityList).isNotNull();
        Assertions.assertThat(savedPilotLanguageEntityList.size()).isEqualTo(2);
    }

    @Test
    public void PilotLanguageRepository_FindLanguagesById()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);

        PilotLanguageEntity pilotLanguageEntity1 = new PilotLanguageEntity((new PilotLanguagePK(1, "English")));
        PilotLanguageEntity pilotLanguageEntity2 = new PilotLanguageEntity((new PilotLanguagePK(1, "German")));

        List<PilotLanguageEntity> pilotLanguageEntityList = new ArrayList<>();
        pilotLanguageEntityList.add(pilotLanguageEntity1);
        pilotLanguageEntityList.add(pilotLanguageEntity2);

        pilotLanguageRepository.saveAll(pilotLanguageEntityList);

        List<PilotLanguageEntity> pilotLanguageEntities = pilotLanguageRepository.findPilotLanguageEntitiesByPilotLanguagePK_PilotId(1);

        Assertions.assertThat(pilotLanguageEntities).isNotNull();
        Assertions.assertThat(pilotLanguageEntities.size()).isEqualTo(2);
    }

    /*
    @Test
    public void PilotLanguagesRepository_DeletePilotById()
    {
        PilotEntity pilotEntity = new PilotEntity(1,"email@gmail.com", "password","first name", "surname", 30, "male", 5000, "Alien", "Senior");
        PilotEntity savedPilotEntity = pilotRepository.save(pilotEntity);

        PilotLanguageEntity pilotLanguageEntity1 = new PilotLanguageEntity(new PilotLanguagePK(1, "English"));

        pilotLanguageRepository.save(pilotLanguageEntity1);

        entityManager.flush();
        entityManager.clear();

        pilotRepository.deleteById(1);

        List<PilotLanguageEntity> pilotLanguage = pilotLanguageRepository.findAll();

        Assertions.assertThat(pilotLanguage.size()).isEqualTo(0);
    }

     */
}
