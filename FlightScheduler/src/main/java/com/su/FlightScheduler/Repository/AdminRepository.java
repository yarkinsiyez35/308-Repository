package com.su.FlightScheduler.Repository;

import com.su.FlightScheduler.Entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    @Transactional(readOnly = true)
    Optional<AdminEntity> findByEmailAndPassword(String email, String password);
    Optional<AdminEntity> findAdminEntityByEmail(String email);
}