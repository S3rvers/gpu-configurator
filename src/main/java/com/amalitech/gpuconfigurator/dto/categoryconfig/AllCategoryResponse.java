package com.amalitech.gpuconfigurator.dto.categoryconfig;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoryResponse {
    private UUID id;
    private String categoryName;
}
