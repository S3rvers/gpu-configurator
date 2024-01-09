package com.amalitech.gpuconfigurator.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(indexName = "products")
public class ProductDocument {
    private UUID id;

    private String productName;

    private String productId;

    private String productDescription;

    private Double productPrice;

    private String productBrand;

    private Integer inStock;

    private Boolean featured;

    private List<String> imageUrl;

    private String coverImage;

    private Boolean productAvailability;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
