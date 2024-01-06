package com.amalitech.gpuconfigurator.service.featured;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.model.Product;

import java.util.List;
import java.util.UUID;

public interface FeaturedService {
    List<Product> getAllFeaturedProduct();

    FeaturedResponseDto addFeaturedProduct(UUID id);

    FeaturedResponseDto removeFeaturedProduct(UUID id);
}
