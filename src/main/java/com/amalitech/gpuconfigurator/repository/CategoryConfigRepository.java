package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.CategoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryConfigRepository extends JpaRepository<CategoryConfig, UUID> {
   Optional<CategoryConfig> findByCategoryId(UUID uuid);
}
