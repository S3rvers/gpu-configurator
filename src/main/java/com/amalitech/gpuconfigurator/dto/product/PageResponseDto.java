package com.amalitech.gpuconfigurator.dto.product;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto {
    private List<ProductResponse> products;
    private long total;
}
