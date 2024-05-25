package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinRepositoryTests {

    @Autowired
    private CabinCrewRepository cabinCrewRepository;

    @Test
    public void CabinCrewRepository_FindCabinCrewEntityByEmailAndPassword() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "First", "Last", 30, "Male", "Nationality", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> cabinCrew = cabinCrewRepository.findCabinCrewEntityByEmailAndPassword("email@gmail.com", "password");

        Assertions.assertThat(cabinCrew.isPresent()).isTrue();
        Assertions.assertThat(cabinCrew.get().getEmail()).isEqualTo("email@gmail.com");
    }

    @Test
    public void CabinCrewRepository_FindByAttendantId() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "First", "Last", 30, "Male", "Nationality", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> cabinCrew = cabinCrewRepository.findByAttendantId(1);

        Assertions.assertThat(cabinCrew.isPresent()).isTrue();
        Assertions.assertThat(cabinCrew.get().getAttendantId()).isEqualTo(1);
    }

    @Test
    public void CabinCrewRepository_FindCabinCrewEntityByEmail() {
        CabinCrewEntity cabinCrewEntity = new CabinCrewEntity(1, "email@gmail.com", "password", "First", "Last", 30, "Male", "Nationality", "Senior");
        cabinCrewRepository.save(cabinCrewEntity);

        Optional<CabinCrewEntity> cabinCrew = cabinCrewRepository.findCabinCrewEntityByEmail("email@gmail.com");

        Assertions.assertThat(cabinCrew.isPresent()).isTrue();
        Assertions.assertThat(cabinCrew.get().getEmail()).isEqualTo("email@gmail.com");
    }

    @Test
    public void CabinCrewRepository_FindCabinCrewEntityBySeniority() {
        CabinCrewEntity cabinCrewEntity1 = new CabinCrewEntity(1, "email1@gmail.com", "password1", "First1", "Last1", 30, "Male", "Nationality1", "Senior");
        CabinCrewEntity cabinCrewEntity2 = new CabinCrewEntity(2, "email2@gmail.com", "password2", "First2", "Last2", 25, "Female", "Nationality2", "Senior");
        cabinCrewRepository.save(cabinCrewEntity1);
        cabinCrewRepository.save(cabinCrewEntity2);

        List<CabinCrewEntity> cabinCrewList = cabinCrewRepository.findCabinCrewEntityBySeniority("Senior");

        Assertions.assertThat(cabinCrewList).isNotEmpty();
        Assertions.assertThat(cabinCrewList.size()).isEqualTo(2);
        Assertions.assertThat(cabinCrewList.get(0).getSeniority()).isEqualTo("Senior");
        Assertions.assertThat(cabinCrewList.get(1).getSeniority()).isEqualTo("Senior");
    }
}
