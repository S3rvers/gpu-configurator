package com.amalitech.gpuconfigurator.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateProductResponseDto {
    private String id;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productCategory;
    private String productId;
    private Boolean productAvailability;
    private LocalDateTime createdAt;
    private List<String> imageUrl;
    private String coverImage;
    private Integer inStock;
}
