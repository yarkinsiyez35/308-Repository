package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.PilotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinCrewRepositoryTests {

    @Autowired
    private CabinCrewRepository cabinCrewRepository;

    @Test
    public void CabinCrewRepository_SaveAttendant() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");

        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);

        assertThat(savedCabinCrewEntity).isNotNull();
        assertThat(savedCabinCrewEntity.getAttendantId()).isEqualTo(1);
    }

    @Test
    public void CabinCrewRepository_FindAttendantById() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");

        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> foundCabinCrewEntity = cabinCrewRepository.findById(1);

        assertThat(foundCabinCrewEntity.isPresent()).isTrue();
        assertThat(foundCabinCrewEntity.get().getAttendantId()).isEqualTo(1);
    }

    @Test
    public void CabinCrewRepository_FindByEmail() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> result = cabinCrewRepository.findCabinCrewEntityByEmail("email@gmail.com");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getEmail()).isEqualTo("email@gmail.com");
    }

    @Test
    public void CabinCrewRepository_FindByEmailAndPassword() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> result = cabinCrewRepository.findCabinCrewEntityByEmailAndPassword("email@gmail.com", "password");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getEmail()).isEqualTo("email@gmail.com");
        assertThat(result.get().getPassword()).isEqualTo("password");
    }
    @Test
    public void CabinCrewRepository_UpdateAttendant() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");

        CabinCrewEntity savedCabinCrewEntity = cabinCrewRepository.save(cabinCrewEntity);
        savedCabinCrewEntity.setAge(45);

        CabinCrewEntity updatedCabinCrewEntity = cabinCrewRepository.save(savedCabinCrewEntity);

        assertThat(updatedCabinCrewEntity).isNotNull();
        assertThat(updatedCabinCrewEntity.getAge()).isEqualTo(45);
    }

    @Test
    public void CabinCrewRepository_DeleteAttendant() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");

        cabinCrewRepository.save(cabinCrewEntity);

        cabinCrewRepository.deleteById(1);

        Optional<CabinCrewEntity> deletedCabinCrew = cabinCrewRepository.findById(1);

        assertThat(deletedCabinCrew.isEmpty()).isTrue();
    }


    @Test
    public void CabinCrewRepository_SaveAll() {
        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity(1, "email1@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        CabinCrewEntity cabinCrewEntity2 = new CabinCrewEntity(2, "email2@gmail.com", "password", "Jane", "Smith", 29, "Female", "Canadian", "Junior");

        List<CabinCrewEntity> cabinCrewEntityList = new ArrayList<>();
        cabinCrewEntityList.add(cabinCrewEntity1);
        cabinCrewEntityList.add(cabinCrewEntity2);

        List<CabinCrewEntity> savedCabinCrewEntityList = cabinCrewRepository.saveAll(cabinCrewEntityList);

        assertThat(savedCabinCrewEntityList).isNotNull();
        assertThat(savedCabinCrewEntityList.size()).isEqualTo(2);
    }

    @Test
    public void CabinCrewRepository_FindAll() {
        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity(1, "email1@gmail.com", "password", "John", "Doe", 34, "Male", "American", "Senior");
        CabinCrewEntity cabinCrewEntity2 = new CabinCrewEntity(2, "email2@gmail.com", "password", "Jane", "Smith", 29, "Female", "Canadian", "Junior");

        List<CabinCrewEntity> cabinCrewEntityList = new ArrayList<>();
        cabinCrewEntityList.add(cabinCrewEntity1);
        cabinCrewEntityList.add(cabinCrewEntity2);

        cabinCrewRepository.saveAll(cabinCrewEntityList);

        List<CabinCrewEntity> savedCabinCrewEntityList = cabinCrewRepository.findAll();

        assertThat(savedCabinCrewEntityList).isNotNull();
        assertThat(savedCabinCrewEntityList.size()).isEqualTo(2);
    }
}

