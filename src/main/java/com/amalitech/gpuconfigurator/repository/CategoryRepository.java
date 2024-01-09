package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
   Optional<Category> findByCategoryName(String categoryName);

    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
    List<Category> findByCategoryNameList(String categoryName);

}
