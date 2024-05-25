package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantVehicleTypeEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantVehicleTypePK;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinVehicleTypeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CabinVehicleTypeRepositoryTests {

    @Autowired
    private CabinVehicleTypeRepository cabinVehicleTypeRepository;

    @Test
    public void CabinVehicleTypeRepository_FindAttendantVehicleTypeEntitiesByAttendantVehicleTypePK_AttendantId() {
        AttendantVehicleTypePK attendantVehicleTypePK = new AttendantVehicleTypePK(1, "Boeing 747");

        AttendantVehicleTypeEntity attendantVehicleTypeEntity = new AttendantVehicleTypeEntity(attendantVehicleTypePK);

        cabinVehicleTypeRepository.save(attendantVehicleTypeEntity);

        List<AttendantVehicleTypeEntity> attendantVehicleTypes = cabinVehicleTypeRepository.findAttendantVehicleTypeEntitiesByAttendantVehicleTypePK_AttendantId(1);

        Assertions.assertThat(attendantVehicleTypes).isNotEmpty();
        Assertions.assertThat(attendantVehicleTypes.size()).isEqualTo(1);
        Assertions.assertThat(attendantVehicleTypes.get(0).getAttendantVehicleTypePK().getAttendantId()).isEqualTo(1);
        Assertions.assertThat(attendantVehicleTypes.get(0).getAttendantVehicleTypePK().getVehicleType()).isEqualTo("Boeing 747");
    }
}
