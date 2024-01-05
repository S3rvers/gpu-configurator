package com.amalitech.gpuconfigurator.dto.categoryconfig;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionResponseDto(String id , String name, String type, BigDecimal price,
                                  String media, String unit, Boolean isCompatible, Boolean isIncluded) {
}
