package com.amalitech.gpuconfigurator.dto.categoryconfig;


import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CompatibleOptionGetResponse(String name, String id, List<CompatibleOptionResponseDto> config) {
}