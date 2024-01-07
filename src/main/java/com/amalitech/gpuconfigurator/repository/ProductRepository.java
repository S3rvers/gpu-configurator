package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {


    @Modifying
    void deleteById(UUID id);

    @Query("SELECT i FROM Product i WHERE i.productId =?1")
    Optional<Product> getProductByProductId(String productId);

    @Query("SELECT p FROM Product p WHERE p.featured = true")
    Optional<List<Product>> getFeaturedProduct();

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :startDate")
    Optional<List<Product>> getBrandNewProducts(@Param("startDate") LocalDateTime startDate);
}
