package com.amalitech.gpuconfigurator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_instock", nullable = false)
    private Integer inStock;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(
            name = "category_id_fk"
    ))
    private Category category;

    @Column(name = "product_featured")
    private Boolean featured;

    @Column(name = "image_url", nullable = false)
    private List<String> imageUrl;

    @Column(name = "product_brand", nullable = false)
    private String productBrand;

    @Column(name = "cover_image_url", nullable = false)
    private String coverImage;

    @Column(name = "product_availability", nullable = false)
    private Boolean productAvailability;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        productAvailability = true;
        featured = false;
    }

}

