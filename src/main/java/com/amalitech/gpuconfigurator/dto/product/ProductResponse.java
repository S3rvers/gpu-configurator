package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponse(
        String id,
        String productName,
        BigDecimal productPrice,
        List<String> imageUrl,

        String coverImage,
        String productId,
        AttributeResponseDto category,
        String productDescription,

        Boolean isFeatured,

        boolean productAvailability,


        Integer inStock
) {
}
