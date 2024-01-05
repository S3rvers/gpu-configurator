package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.CompatibleOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompatibleOptionRepository extends JpaRepository<CompatibleOption, UUID> {
    List<CompatibleOption> getByCategoryConfigId(UUID id);

    @Transactional
    @Modifying
    void deleteAllByCategoryConfigId(UUID categoryConfigId);
}
