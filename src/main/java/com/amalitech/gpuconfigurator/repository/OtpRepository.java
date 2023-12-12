package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {

}