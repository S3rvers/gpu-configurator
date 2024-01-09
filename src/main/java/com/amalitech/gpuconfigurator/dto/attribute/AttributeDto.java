package com.amalitech.gpuconfigurator.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttributeDto(
        @NotNull(message = "attribute should not be null")
                @NotBlank(message = "attribute should not be blank")
       String attributeName
) {
}
