package com.amalitech.gpuconfigurator.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductUpdateDto {
    String productName;
    String productDescription;
    Double productPrice;
    String productId;
    Boolean availability;
    Integer inStock;
    String category;
    String brand;
}