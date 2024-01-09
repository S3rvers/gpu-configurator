package com.amalitech.gpuconfigurator.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrandRequest(
        @NotNull(message = "brand name cannot be null")
                @NotBlank(message = "brand name cannot be blank")
        String name

) {
}
