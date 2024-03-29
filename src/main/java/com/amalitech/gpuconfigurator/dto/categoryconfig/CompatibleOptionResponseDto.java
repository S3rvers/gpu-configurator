package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionResponseDto(String compatibleOptionId, String name, String type, BigDecimal price,
                                  String media, String unit, Boolean isCompatible, Boolean isIncluded, Boolean isMeasured, Double priceFactor, Integer size, String attributeId, String attributeOptionId,  Float priceIncrement, Float baseAmount, Float maxAmount, Integer inStock) {
}
