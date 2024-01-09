package com.amalitech.gpuconfigurator.dto.brand;

import jakarta.validation.constraints.NotNull;

public record BrandDto(@NotNull  String name, String id) {
}
