package com.su.FlightScheduler.RepositoryTest;

import com.su.FlightScheduler.Entity.AdminEntity;
import com.su.FlightScheduler.Repository.AdminRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminRepositoryTests {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void AdminRepository_FindByEmailAndPassword() {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setEmail("email@gmail.com");
        adminEntity.setPassword("password");
        adminRepository.save(adminEntity);

        Optional<AdminEntity> admin = adminRepository.findByEmailAndPassword("email@gmail.com", "password");

        Assertions.assertThat(admin.isPresent()).isEqualTo(true);
        Assertions.assertThat(admin.get().getEmail()).isEqualTo("email@gmail.com");
    }

    @Test
    public void AdminRepository_FindAdminEntityByEmail() {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setEmail("email@gmail.com");
        adminEntity.setPassword("password");
        adminRepository.save(adminEntity);

        Optional<AdminEntity> admin = adminRepository.findAdminEntityByEmail("email@gmail.com");

        Assertions.assertThat(admin.isPresent()).isEqualTo(true);
        Assertions.assertThat(admin.get().getEmail()).isEqualTo("email@gmail.com");
    }
}

