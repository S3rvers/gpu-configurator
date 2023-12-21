package com.amalitech.gpuconfigurator.repository.category;

import com.amalitech.gpuconfigurator.model.category.CategoryConfigOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryConfigOptionRepository extends JpaRepository<CategoryConfigOption, UUID> {
}
