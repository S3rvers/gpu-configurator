package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.model.CategoryConfig;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CompatibleOptionDTO(String name, String type, CategoryConfig categoryConfig, BigDecimal price,
                                  String media, String unit, Boolean isCompatible, Boolean isIncluded) {
}
