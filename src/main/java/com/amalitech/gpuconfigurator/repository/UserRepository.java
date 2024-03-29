package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    BasicInformationResponse findBasicInformationByEmail(String email);
}
