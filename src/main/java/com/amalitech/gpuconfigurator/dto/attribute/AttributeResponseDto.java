package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;

@Builder
public record AttributeResponseDto(String name, String id) {
}
